package com.example.trainstationapp.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.trainstationapp.core.state.State
import com.example.trainstationapp.data.database.Database
import com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationAPIGrpcKt
import com.example.trainstationapp.data.grpc.trainstation.v1alpha1.getOneStationRequest
import com.example.trainstationapp.data.grpc.trainstation.v1alpha1.setFavoriteOneStationRequest
import com.example.trainstationapp.domain.entities.Station
import com.example.trainstationapp.domain.repositories.StationRepository
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class StationRepositoryImpl
@Inject
constructor(
    private val stationAPI: StationAPIGrpcKt.StationAPICoroutineStub,
    private val database: Database
) : StationRepository {
    companion object {
        private const val NETWORK_PAGE_SIZE = 20
    }

    /**
     * Fetch the whole `StationModel` cache as a `PagingData`.
     *
     * Receive a `PagingData` for each page. This is a snapshot of the cache. To refresh the
     * `PagingData`, call the method again.
     */
    @OptIn(ExperimentalPagingApi::class)
    override fun watchPages(search: String, token: String): Flow<PagingData<Station>> {
        val bearer = "Bearer $token"
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
                        token = bearer
                    ),
                pagingSourceFactory = pagingSourceFactory
            )
            .flow
            .flowOn(Dispatchers.Default)
    }

    /** Observe one station in the cache. */
    override fun watchOne(station: Station): Flow<State<Station>> {
        return database
            .stationDao()
            .watchById(station.id)
            .map { State.Success(it) }
            .catch<State<Station>> { emit(State.Failure(it)) }
    }

    /** Find one station in the API and cache it. */
    override suspend fun findOne(station: Station, token: String): State<Station> {
        val bearer = "Bearer $token"
        return try {
            val response =
                stationAPI.getOneStation(
                    getOneStationRequest {
                        id = station.id
                        this.token = bearer
                    }
                )
            response.station?.let {
                val entity = Station.fromGrpc(it)
                database.stationDao().insert(entity)
                State.Success(entity)
            }
                ?: State.Failure(Exception("Element not found."))
        } catch (e: Throwable) {
            State.Failure(e)
        }
    }

    /** Update one station in the API and cache it. */
    override suspend fun makeFavoriteOne(
        id: String,
        value: Boolean,
        token: String
    ): State<Station> {
        val bearer = "Bearer $token"
        return try {
            stationAPI.setFavoriteOneStation(
                setFavoriteOneStationRequest {
                    this.id = id
                    this.token = bearer
                    this.value = value
                }
            )
            val resp =
                stationAPI.getOneStation(
                    getOneStationRequest {
                        this.id = id
                        this.token = bearer
                    }
                )
            resp.station?.let {
                val entity = Station.fromGrpc(it)
                database.stationDao().insert(entity)
                State.Success(entity)
            }
                ?: State.Failure(Exception("Element not found."))
        } catch (e: Throwable) {
            State.Failure(e)
        }
    }
}
