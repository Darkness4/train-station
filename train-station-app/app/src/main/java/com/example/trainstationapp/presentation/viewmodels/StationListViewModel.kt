package com.example.trainstationapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.trainstationapp.domain.entities.Station
import com.example.trainstationapp.domain.repositories.StationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class StationListViewModel @Inject constructor(private val repository: StationRepository) :
    ViewModel() {

    private val _search = MutableStateFlow("")
    val search: StateFlow<String>
        get() = _search

    fun updateSearch(s: String) {
        _search.value = s
    }

    /** Fetch the `PagingData`. */
    @OptIn(ExperimentalCoroutinesApi::class)
    val pages: StateFlow<PagingData<Station>> =
        search
            .flatMapLatest { s -> repository.watchPages(s).cachedIn(viewModelScope) }
            .catch { e ->
                Timber.e(e)
                _errorState.value = e.toString()
            }
            .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    private val _errorState = MutableStateFlow<String?>(null)
    val error: StateFlow<String?>
        get() = _errorState

    fun clearError() {
        _errorState.value = null
    }

    fun makeFavorite(id: String, value: Boolean) =
        viewModelScope.launch(Dispatchers.Main) {
            try {
                repository.makeFavoriteOne(id, value)
            } catch (e: Throwable) {
                timber.log.Timber.e(e)
                _errorState.value = e.toString()
            }
        }
}
