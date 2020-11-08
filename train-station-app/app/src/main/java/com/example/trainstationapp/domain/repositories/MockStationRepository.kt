package com.example.trainstationapp.domain.repositories

import com.example.trainstationapp.core.result.Result
import com.example.trainstationapp.domain.entities.Station
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map

class MockStationRepository : StationRepository {
    private val _db = mutableListOf<Station>()

    @ExperimentalCoroutinesApi
    private val stationsFlow = MutableSharedFlow<List<Station>>()

    @ExperimentalCoroutinesApi
    override fun watch(): Flow<Result<List<Station>>> = stationsFlow.map { Result.Success(it) }

    @ExperimentalCoroutinesApi
    override suspend fun refreshAll(): Result<Unit> {
        stationsFlow.emit(_db)
        return Result.Success(Unit)
    }

    @ExperimentalCoroutinesApi
    override suspend fun createOne(station: Station): Result<Unit> {
        _db.add(station)
        stationsFlow.emit(_db)
        return Result.Success(Unit)
    }

    @ExperimentalCoroutinesApi
    override suspend fun replaceOne(station: Station): Result<Unit> {
        _db.find { it.recordid == station.recordid }?.let { _db.remove(it) }
        _db.add(station)
        stationsFlow.emit(_db)
        return Result.Success(Unit)
    }
}
