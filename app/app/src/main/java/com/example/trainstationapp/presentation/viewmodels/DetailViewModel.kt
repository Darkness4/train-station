package com.example.trainstationapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trainstationapp.domain.entities.Station
import com.example.trainstationapp.domain.repositories.StationRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber

class DetailViewModel
@AssistedInject
constructor(stationRepository: StationRepository, @Assisted initialStationId: String) :
    ViewModel() {

    val station: StateFlow<Station?> =
        stationRepository
            .watchOne(initialStationId)
            .catch { e ->
                Timber.e(e)
                _errorState.value = e.toString()
            }
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
        fun AssistedFactory.provideFactory(initialStationId: String): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return this@provideFactory.create(initialStationId) as T
                }
            }
    }
}
