package com.example.trainstationapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trainstationapp.core.state.State
import com.example.trainstationapp.core.state.map
import com.example.trainstationapp.domain.entities.Station
import com.example.trainstationapp.domain.repositories.StationRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetailsViewModel
@AssistedInject
constructor(
    private val stationRepository: StationRepository,
    @Assisted initialStation: Station,
    @Assisted token: String
) : ViewModel() {
    init {
        fetch(initialStation, token)
    }

    private val _networkStatus = MutableStateFlow<State<Unit>?>(null)
    val networkStatus: StateFlow<State<Unit>?>
        get() = _networkStatus

    val station =
        stationRepository
            .watchOne(initialStation)
            .mapNotNull { it.getOrNull() } // Only take successful values
            .filter { it.fields != null && it.geometry != null } // Make sure data is complete
            .stateIn(viewModelScope, SharingStarted.Lazily, null)

    private fun fetch(station: Station, token: String) {
        viewModelScope.launch(Dispatchers.Main) {
            _networkStatus.value = stationRepository.findOne(station, token).map {}
        }
    }

    @dagger.assisted.AssistedFactory
    fun interface AssistedFactory {
        fun create(initialStation: Station, token: String): DetailsViewModel
    }

    companion object {
        fun AssistedFactory.provideFactory(
            initialStation: Station,
            token: String
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    return this@provideFactory.create(initialStation, token) as T
                }
            }
    }
}
