package com.example.trainstationapp.domain.repositories

import androidx.paging.PagingData
import com.example.trainstationapp.core.state.State
import com.example.trainstationapp.domain.entities.Station
import kotlinx.coroutines.flow.Flow

interface StationRepository {
    fun watchPages(search: String, token: String): Flow<PagingData<Station>>
    fun watchOne(station: Station): Flow<State<Station>>
    suspend fun findOne(station: Station, token: String): State<Station>
    suspend fun createOne(station: Station, token: String): State<Station>
    suspend fun makeFavoriteOne(id: String, value: Boolean, token: String): State<Station>
}
