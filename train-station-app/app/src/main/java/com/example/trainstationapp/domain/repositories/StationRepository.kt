package com.example.trainstationapp.domain.repositories

import com.example.trainstationapp.core.result.Result
import com.example.trainstationapp.domain.entities.Station
import kotlinx.coroutines.flow.Flow

interface StationRepository {
    fun watch(): Flow<Result<List<Station>>>
    suspend fun refreshAll(): Result<Unit>
    suspend fun createOne(station: Station): Result<Unit>
    suspend fun replaceOne(station: Station): Result<Unit>
}
