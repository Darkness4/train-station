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
     *
     * This flow can survives the configuration changes if cached in the CoroutineScope of
     * the ViewModel.
     */
    private var station: Flow<PagingData<Station>>? = null
    private var searchValue: String? = null

    /**
     * Fetch the `PagingData`.
     */
    fun watch(search: String): Flow<PagingData<Station>> {
        val lastResult = station

        // If already fetched
        if (search == searchValue && lastResult != null) {
            return lastResult
        }
        searchValue = search
        val newResult: Flow<PagingData<Station>> = repository.watch(search)
            .cachedIn(viewModelScope) // Cache the content in a CoroutineScope
        station = newResult
        return newResult
    }

    class Factory(private val repository: StationRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return StationViewModel(repository) as T
        }
    }
}
