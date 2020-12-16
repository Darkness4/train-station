package com.example.trainstationapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.trainstationapp.core.result.Result
import com.example.trainstationapp.domain.entities.Station
import com.example.trainstationapp.domain.repositories.StationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: StationRepository) : ViewModel() {
    enum class RefreshMode {
        Normal,
        WithScrollToTop,
    }
    private val _refreshManually = MutableLiveData<RefreshMode?>()
    val refreshManually: LiveData<RefreshMode?>
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

    private val _showDetails = MutableLiveData<Station?>()
    val showDetails: LiveData<Station?>
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
     * This flow can survives the configuration changes if cached in the CoroutineScope of
     * the ViewModel.
     */
    private var station: Flow<PagingData<Station>>? = null
    private var searchValue: String? = null

    /**
     * Fetch the `PagingData`.
     */
    fun watchPages(search: String): Flow<PagingData<Station>> {
        val lastResult = station

        // If already fetched
        if (search == searchValue && lastResult != null) {
            return lastResult
        }
        searchValue = search
        val newResult: Flow<PagingData<Station>> = repository.watchPages(search)
            .cachedIn(viewModelScope) // Cache the content in a CoroutineScope
        station = newResult
        return newResult
    }

    private val _networkStatus = MutableLiveData<Result<Unit>>()
    val networkStatus: LiveData<Result<Unit>>
        get() = _networkStatus

    fun update(station: Station) {
        viewModelScope.launch(Dispatchers.Main) {
            _networkStatus.value = repository.updateOne(station.copy().toggleFavorite()).map { }
            refreshManually()
        }
    }

    class Factory(private val repository: StationRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel(repository) as T
        }
    }
}
