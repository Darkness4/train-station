package com.example.trainstationapp.domain.repositories

import androidx.paging.PagingData
import com.example.trainstationapp.core.result.Result
import com.example.trainstationapp.domain.entities.Station
import kotlinx.coroutines.flow.Flow

interface StationRepository {
    fun watchPages(search: String): Flow<PagingData<Station>>
    fun watchOne(station: Station): Flow<Result<Station>>
    suspend fun findOne(station: Station): Result<Station>
    suspend fun createOne(station: Station): Result<Station>
    suspend fun updateOne(station: Station): Result<Station>
}
