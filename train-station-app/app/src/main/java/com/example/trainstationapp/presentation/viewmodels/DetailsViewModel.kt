package com.example.trainstationapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.trainstationapp.domain.entities.Station
import com.example.trainstationapp.domain.repositories.StationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class DetailsViewModel(
    initialStation : Station,
    private val stationRepository: StationRepository
) : ViewModel() {
    init {
        fetch(initialStation)
    }

    val station = stationRepository.watchOne(initialStation).filter { it.isSuccess }.map { it.valueOrNull() }.asLiveData(viewModelScope.coroutineContext + Dispatchers.Default)
    private fun fetch (station: Station) = viewModelScope.launch(Dispatchers.Main) { stationRepository.findOne(station) }

    class Factory(private val initialStation: Station, private val repository: StationRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return DetailsViewModel(initialStation, repository) as T
        }
    }
}