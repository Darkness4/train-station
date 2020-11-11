package com.example.trainstationapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.trainstationapp.domain.entities.Station
import com.example.trainstationapp.domain.repositories.StationRepository
import kotlinx.coroutines.flow.Flow

class StationViewModel(private val repository: StationRepository) : ViewModel() {

    /**
     * Observable train stations.
     */
    private var station: Flow<PagingData<Station>>? = null

    fun fetch(): Flow<PagingData<Station>> {
        val lastResult = station
        if (lastResult != null) {
            return lastResult
        }
        val newResult: Flow<PagingData<Station>> = repository.watch()
            .cachedIn(viewModelScope)
        station = newResult
        return newResult
    }

    class Factory(private val repository: StationRepository): ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return StationViewModel(repository) as T
        }
    }
}