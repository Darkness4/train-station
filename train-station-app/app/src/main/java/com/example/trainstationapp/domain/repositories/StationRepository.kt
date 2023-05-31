package com.example.trainstationapp.domain.repositories

import androidx.paging.PagingData
import com.example.trainstationapp.domain.entities.Station
import kotlinx.coroutines.flow.Flow

interface StationRepository {
    fun watchPages(search: String): Flow<PagingData<Station>>

    fun watchOne(id: String): Flow<Station>

    suspend fun makeFavoriteOne(id: String, value: Boolean): Station
}
