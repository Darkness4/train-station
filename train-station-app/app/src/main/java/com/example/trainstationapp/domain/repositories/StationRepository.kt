package com.example.trainstationapp.domain.repositories

import androidx.paging.PagingData
import com.example.trainstationapp.core.result.Result
import com.example.trainstationapp.domain.entities.Station
import kotlinx.coroutines.flow.Flow

interface StationRepository {
    fun watch(): Flow<PagingData<Station>>
    suspend fun createOne(station: Station): Result<Unit>
    suspend fun replaceOne(station: Station): Result<Unit>
}
