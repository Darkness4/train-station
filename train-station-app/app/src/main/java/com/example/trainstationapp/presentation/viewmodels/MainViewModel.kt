package com.example.trainstationapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trainstationapp.domain.entities.Station

class MainViewModel : ViewModel() {
    private val _refreshManually = MutableLiveData<Unit?>()

    val refreshManually: LiveData<Unit?>
        get() = _refreshManually

    fun refreshManually() {
        _refreshManually.value = Unit
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


}
