package com.example.trainstationapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.trainstationapp.core.state.State
import com.example.trainstationapp.core.state.map
import com.example.trainstationapp.domain.entities.Station
import com.example.trainstationapp.domain.repositories.StationRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch

class DetailsViewModel @AssistedInject constructor(
    private val stationRepository: StationRepository,
    @Assisted initialStation: Station,
) : ViewModel() {
    init {
        fetch(initialStation)
    }

    private val _networkStatus = MutableLiveData<State<Unit>>()
    val networkStatus: LiveData<State<Unit>>
        get() = _networkStatus

    val station =
        stationRepository.watchOne(initialStation)
            .mapNotNull { it.getOrNull() } // Only take successful values
            .filter { it.fields != null && it.geometry != null } // Make sure data is complete
            .asLiveData(viewModelScope.coroutineContext + Dispatchers.Default)

    private fun fetch(station: Station) {
        viewModelScope.launch(Dispatchers.Main) {
            _networkStatus.value = stationRepository.findOne(station).map { }
        }
    }

    @dagger.assisted.AssistedFactory
    fun interface AssistedFactory {
        fun create(initialStation: Station): DetailsViewModel
    }

    companion object {
        fun AssistedFactory.provideFactory(initialStation: Station): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    return this@provideFactory.create(initialStation) as T
                }
            }
    }
}
