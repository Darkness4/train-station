package com.example.trainstationapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.trainstationapp.core.state.State
import com.example.trainstationapp.core.state.map
import com.example.trainstationapp.domain.entities.Station
import com.example.trainstationapp.domain.repositories.StationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: StationRepository) : ViewModel() {
    enum class RefreshMode {
        Normal,
        WithScrollToTop,
    }

    private val _refreshManually = MutableStateFlow<RefreshMode?>(null)
    val refreshManually: StateFlow<RefreshMode?>
        get() = _refreshManually

    private fun refreshManually() {
        _refreshManually.value = RefreshMode.Normal
    }

    fun refreshManuallyAndScrollToTop() {
        _refreshManually.value = RefreshMode.WithScrollToTop
    }

    fun refreshManuallyDone() {
        _refreshManually.value = null
    }

    private val _showDetails = MutableStateFlow<Station?>(null)
    val showDetails: StateFlow<Station?>
        get() = _showDetails

    fun showDetails(station: Station) {
        _showDetails.value = station
    }

    fun showDetailsDone() {
        _showDetails.value = null
    }

    /**
     * Observable train stations.
     *
     * This flow can survives the configuration changes if cached in the CoroutineScope of the
     * ViewModel.
     */
    private var station: Flow<PagingData<Station>>? = null
    private var searchValue: String? = null

    /** Fetch the `PagingData`. */
    fun watchPages(search: String, token: String): Flow<PagingData<Station>> {
        val lastResult = station

        // If already fetched
        if (search == searchValue && lastResult != null) {
            return lastResult
        }
        searchValue = search
        val newResult: Flow<PagingData<Station>> =
            repository
                .watchPages(search, token)
                .cachedIn(viewModelScope) // Cache the content in a CoroutineScope
        station = newResult
        return newResult
    }

    private val _networkStatus = MutableStateFlow<State<Unit>?>(null)
    val networkStatus: StateFlow<State<Unit>?>
        get() = _networkStatus

    fun makeFavorite(id: String, value: Boolean, token: String) =
        viewModelScope.launch(Dispatchers.Main) {
            _networkStatus.value = repository.makeFavoriteOne(id, value, token).map {}
            refreshManually()
        }
}
