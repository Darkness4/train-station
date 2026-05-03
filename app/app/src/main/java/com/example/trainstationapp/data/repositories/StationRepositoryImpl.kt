package com.example.trainstationapp.data.repositories

import androidx.datastore.core.DataStore
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.connectrpc.getOrThrow
import com.example.trainstationapp.data.database.Database
import com.example.trainstationapp.data.datastore.Session
import com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationAPIClientInterface
import com.example.trainstationapp.data.grpc.trainstation.v1alpha1.getOneStationRequest
import com.example.trainstationapp.data.grpc.trainstation.v1alpha1.setFavoriteOneStationRequest
import com.example.trainstationapp.domain.entities.Station
import com.example.trainstationapp.domain.repositories.StationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StationRepositoryImpl
@Inject
constructor(
    private val stationAPI: StationAPIClientInterface,
    private val database: Database,
    private val oauthDataStore: DataStore<Session.OAuth>,
) : StationRepository {
    companion object {
        private const val NETWORK_PAGE_SIZE = 50
    }

    /**
     * Fetch the whole `StationModel` cache as a `PagingData`.
     *
     * Receive a `PagingData` for each page. This is a snapshot of the cache. To refresh the
     * `PagingData`, call the method again.
     */
    @OptIn(ExperimentalPagingApi::class)
    override fun watchPages(search: String): Flow<PagingData<Station>> {
        val pagingSourceFactory = {
            if (search.isNotEmpty()) {
                database.stationDao().watchAsPagingSource(search)
            } else {
                database.stationDao().watchAsPagingSource()
            }
        }
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator =
            StationRemoteMediator(
                search = search,
                oauthDataStore = oauthDataStore,
                database = database,
                stationAPI = stationAPI,
            ),
            pagingSourceFactory = pagingSourceFactory,
        )
            .flow
            .flowOn(Dispatchers.Default)
    }

    /** Observe one station in the cache or the api. */
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun watchOne(id: String): Flow<Station> = oauthDataStore.data.flatMapLatest { jwt ->
        if (jwt.expiresAt <= System.currentTimeMillis()) {
            throw IllegalStateException("Invalid token")
        }
        // Try to insert in database
        val response =
            stationAPI.getOneStation(
                getOneStationRequest {
                    this.id = id
                },
                headers = mapOf(
                    "Authorization" to listOf("Bearer ${jwt.accessToken}"),
                ),
            )
        response.getOrThrow().station?.let {
            val entity = Station.fromGrpc(it)
            database.stationDao().insert(entity)
        } ?: throw Exception("Element not found.")

        database.stationDao().watchById(id)
    }

    /** Update one station in the API and cache it. */
    override suspend fun makeFavoriteOne(id: String, value: Boolean): Station {
        val jwt = oauthDataStore.data.map { if (it.expiresAt > System.currentTimeMillis()) it else null }.first() ?: run {
            throw IllegalStateException("Invalid token")
        }

        stationAPI.setFavoriteOneStation(
            setFavoriteOneStationRequest {
                this.id = id
                this.value = value
            },
            headers = mapOf(
                "Authorization" to listOf("Bearer ${jwt.accessToken}"),
            ),
        )
        val resp =
            stationAPI.getOneStation(
                getOneStationRequest {
                    this.id = id
                },
                headers = mapOf(
                    "Authorization" to listOf("Bearer ${jwt.accessToken}"),
                ),
            )
        return resp.getOrThrow().station?.let {
            val entity = Station.fromGrpc(it)
            database.stationDao().insert(entity)
            entity
        } ?: throw Exception("Element not found.")
    }
}
