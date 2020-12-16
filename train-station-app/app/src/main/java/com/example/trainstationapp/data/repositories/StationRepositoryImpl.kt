package com.example.trainstationapp.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.trainstationapp.core.result.Result
import com.example.trainstationapp.data.database.Database
import com.example.trainstationapp.data.datasources.TrainStationDataSource
import com.example.trainstationapp.domain.entities.Station
import com.example.trainstationapp.domain.repositories.StationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StationRepositoryImpl @Inject constructor(
    private val trainStationDataSource: TrainStationDataSource,
    private val database: Database,
) : StationRepository {
    companion object {
        private const val NETWORK_PAGE_SIZE = 20
    }

    /**
     * Fetch the whole `StationModel` cache as a `PagingData`.
     *
     * Receive a `PagingData` for each page. This is a snapshot of the cache.
     * To refresh the `PagingData`, call the method again.
     */
    @ExperimentalPagingApi
    override fun watchPages(search: String): Flow<PagingData<Station>> {
        val pagingSourceFactory = {
            if (search.isNotEmpty()) {
                database.stationDao().watchAsPagingSource(search)
            } else {
                database.stationDao().watchAsPagingSource()
            }
        }
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = StationRemoteMediator(
                search = search,
                service = trainStationDataSource,
                database = database,
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
            .map {
                it.map { model -> model.asEntity() }
            }.flowOn(Dispatchers.Default)
    }

    /**
     * Observe one station in the cache.
     */
    override fun watchOne(station: Station): Flow<Result<Station>> {
        return database.stationDao().watchById(station.recordid).map { model ->
            Result.Success(model.asEntity())
        }.catch<Result<Station>> {
            emit(Result.Failure(it))
        }
    }

    /**
     * Find one station in the API and cache it.
     */
    override suspend fun findOne(station: Station): Result<Station> {
        return try {
            val model = trainStationDataSource.findById(station.recordid)
            model?.let {
                database.stationDao().insert(model)
                Result.Success(model.asEntity())
            } ?: Result.Failure(Exception("Element not found."))
        } catch (e: Throwable) {
            Result.Failure(e)
        }
    }

    /**
     * Create one station in the API and cache it.
     */
    override suspend fun createOne(station: Station): Result<Station> {
        return try {
            val model = trainStationDataSource.create(station.asModel())
            database.stationDao().insert(model)
            Result.Success(model.asEntity())
        } catch (e: Throwable) {
            Result.Failure(e)
        }
    }

    /**
     * Update one station in the API and cache it.
     */
    override suspend fun updateOne(station: Station): Result<Station> {
        return try {
            val model = trainStationDataSource.updateById(station.recordid, station.asModel())
            model?.let {
                database.stationDao().insert(model)
                Result.Success(model.asEntity())
            } ?: Result.Failure(Exception("Element not found."))
        } catch (e: Throwable) {
            Result.Failure(e)
        }
    }
}
