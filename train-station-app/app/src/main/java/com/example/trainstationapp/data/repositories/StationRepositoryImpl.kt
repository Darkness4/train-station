package com.example.trainstationapp.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.trainstationapp.data.database.Database
import com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationAPIGrpcKt
import com.example.trainstationapp.data.grpc.trainstation.v1alpha1.getOneStationRequest
import com.example.trainstationapp.data.grpc.trainstation.v1alpha1.setFavoriteOneStationRequest
import com.example.trainstationapp.domain.entities.Station
import com.example.trainstationapp.domain.repositories.StationRepository
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class StationRepositoryImpl
@Inject
constructor(
    private val stationAPI: StationAPIGrpcKt.StationAPICoroutineStub,
    private val database: Database
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
    override fun watchPages(search: String, token: String): Flow<PagingData<Station>> {
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
                        service = stationAPI,
                        database = database,
                        token = token
                    ),
                pagingSourceFactory = pagingSourceFactory
            )
            .flow
            .flowOn(Dispatchers.Default)
    }

    /** Observe one station in the cache or the api. */
    override fun watchOne(id: String, token: String): Flow<Station> = flow {
        // Try to insert in database
        val response =
            stationAPI.getOneStation(
                getOneStationRequest {
                    this.id = id
                    this.token = token
                }
            )
        response.station?.let {
            val entity = Station.fromGrpc(it)
            database.stationDao().insert(entity)
            emit(entity)
        }
            ?: throw Exception("Element not found.")

        database.stationDao().watchById(id).collect { emit(it) }
    }

    /** Update one station in the API and cache it. */
    override suspend fun makeFavoriteOne(id: String, value: Boolean, token: String): Station {
        stationAPI.setFavoriteOneStation(
            setFavoriteOneStationRequest {
                this.id = id
                this.token = token
                this.value = value
            }
        )
        val resp =
            stationAPI.getOneStation(
                getOneStationRequest {
                    this.id = id
                    this.token = token
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
