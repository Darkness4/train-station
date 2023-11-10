package com.example.trainstationapp.data.repositories

import androidx.datastore.core.DataStore
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.trainstationapp.data.database.Database
import com.example.trainstationapp.data.datastore.Session
import com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationAPIGrpcKt
import com.example.trainstationapp.data.grpc.trainstation.v1alpha1.getOneStationRequest
import com.example.trainstationapp.data.grpc.trainstation.v1alpha1.setFavoriteOneStationRequest
import com.example.trainstationapp.domain.entities.Station
import com.example.trainstationapp.domain.repositories.StationRepository
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn

class StationRepositoryImpl
@Inject
constructor(
    private val stationAPI: StationAPIGrpcKt.StationAPICoroutineStub,
    private val database: Database,
    private val jwtDataStore: DataStore<Session.Jwt>,
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
                        jwtDataStore = jwtDataStore,
                        database = database,
                        stationAPI = stationAPI,
                    ),
                pagingSourceFactory = pagingSourceFactory
            )
            .flow
            .flowOn(Dispatchers.Default)
    }

    /** Observe one station in the cache or the api. */
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun watchOne(id: String): Flow<Station> =
        jwtDataStore.data.flatMapLatest { jwt ->
            // Try to insert in database
            val response =
                stationAPI.getOneStation(
                    getOneStationRequest {
                        this.id = id
                        this.token = jwt.token
                    }
                )
            response.station?.let {
                val entity = Station.fromGrpc(it)
                database.stationDao().insert(entity)
            }
                ?: throw Exception("Element not found.")

            database.stationDao().watchById(id)
        }

    /** Update one station in the API and cache it. */
    override suspend fun makeFavoriteOne(id: String, value: Boolean): Station {
        val jwt = jwtDataStore.data.first()

        stationAPI.setFavoriteOneStation(
            setFavoriteOneStationRequest {
                this.id = id
                this.token = jwt.token
                this.value = value
            }
        )
        val resp =
            stationAPI.getOneStation(
                getOneStationRequest {
                    this.id = id
                    this.token = jwt.token
                }
            )
        return resp.station?.let {
            val entity = Station.fromGrpc(it)
            database.stationDao().insert(entity)
            entity
        }
            ?: throw Exception("Element not found.")
    }
}
