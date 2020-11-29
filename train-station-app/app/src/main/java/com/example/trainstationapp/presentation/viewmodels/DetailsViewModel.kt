package com.example.trainstationapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.trainstationapp.core.result.Result
import com.example.trainstationapp.domain.entities.Station
import com.example.trainstationapp.domain.repositories.StationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch

class DetailsViewModel(
    initialStation: Station,
    private val stationRepository: StationRepository
) : ViewModel() {
    init {
        fetch(initialStation)
    }

    private val _networkStatus = MutableLiveData<Result<Unit>>()
    val networkStatus: LiveData<Result<Unit>>
        get() = _networkStatus

    val station =
        stationRepository.watchOne(initialStation)
            .mapNotNull { it.valueOrNull() } // Only take successful values
            .filter { it.fields != null && it.geometry != null } // Make sure data is joined
            .asLiveData(viewModelScope.coroutineContext + Dispatchers.Default)

    private fun fetch(station: Station) {
        viewModelScope.launch(Dispatchers.Main) {
            _networkStatus.value = stationRepository.findOne(station).map { }
        }
    }

    class Factory(private val initialStation: Station, private val repository: StationRepository) :
        ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return DetailsViewModel(initialStation, repository) as T
        }
    }
}
