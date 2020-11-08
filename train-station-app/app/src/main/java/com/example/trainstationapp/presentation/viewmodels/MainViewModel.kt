package com.example.trainstationapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trainstationapp.domain.repositories.StationRepository

class MainViewModel(private val stationRepository: StationRepository) : ViewModel() {





    class Factory(private val stationRepository: StationRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel(stationRepository) as T
        }
    }
}