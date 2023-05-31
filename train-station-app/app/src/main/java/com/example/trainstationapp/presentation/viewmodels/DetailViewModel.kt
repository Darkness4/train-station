package com.example.trainstationapp.presentation.viewmodels

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trainstationapp.data.datastore.Session
import com.example.trainstationapp.domain.entities.Station
import com.example.trainstationapp.domain.repositories.StationRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

class DetailViewModel
@AssistedInject
constructor(
    private val stationRepository: StationRepository,
    jwtDataStore: DataStore<Session.Jwt>,
    @Assisted initialStationId: String,
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val station: StateFlow<Station?> =
        jwtDataStore.data
            .flatMapLatest { stationRepository.watchOne(initialStationId, it.token) }
            .catch { e -> _errorState.value = e.toString() }
            .stateIn(viewModelScope, SharingStarted.Lazily, null)

    private val _errorState = MutableStateFlow<String?>(null)
    val error: StateFlow<String?>
        get() = _errorState

    fun clearError() {
        _errorState.value = null
    }

    @dagger.assisted.AssistedFactory
    fun interface AssistedFactory {
        fun create(initialStationId: String): DetailViewModel
    }

    companion object {
        fun AssistedFactory.provideFactory(
            initialStationId: String,
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return this@provideFactory.create(initialStationId) as T
                }
            }
    }
}
