package com.example.trainstationapp.presentation.viewmodels

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.trainstationapp.data.datastore.Session
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
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class StationListViewModel
@Inject
constructor(jwtDataStore: DataStore<Session.Jwt>, private val repository: StationRepository) :
    ViewModel() {
    private val jwtToken: StateFlow<Session.Jwt?> =
        jwtDataStore.data
            .filter { !it.token.isNullOrEmpty() }
            .catch { e -> _errorState.value = e.toString() }
            .stateIn(viewModelScope, SharingStarted.Lazily, null)

    private val _search = MutableStateFlow("")
    val search: StateFlow<String>
        get() = _search

    fun updateSearch(s: String) {
        _search.value = s
    }

    /** Fetch the `PagingData`. */
    @OptIn(ExperimentalCoroutinesApi::class)
    val pages: StateFlow<PagingData<Station>> =
        jwtToken
            .flatMapLatest {
                search.flatMapLatest { s ->
                    jwtDataStore.data.flatMapLatest { jwt ->
                        repository.watchPages(s, jwt.token).cachedIn(viewModelScope)
                    }
                }
            }
            .catch { e -> _errorState.value = e.toString() }
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
                jwtToken.value?.let { repository.makeFavoriteOne(id, value, it.token) }
                    ?: throw RuntimeException("Not authenticated")
            } catch (e: Throwable) {
                _errorState.value = e.toString()
            }
        }
}
