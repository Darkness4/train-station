package com.example.trainstationapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    // TODO: 1. Ici mettre une action "refreshManually" (LiveData<Unit> + méthode action + méthode réinitialisation)
    private val _refreshManually = MutableLiveData<Unit?>()

    val refreshManually: LiveData<Unit?>
        get() = _refreshManually

    fun refreshManually() {
        _refreshManually.value = Unit
    }

    fun refreshManuallyDone() {
        _refreshManually.value = null
    }

    // TODO: 2. Binder l'action avec un bouton refresh de l'action bar
    // TODO: 3. Faire que dans StationListFragment, on observer l'action
    // TODO: 4. Rafraîchir en lançant la méthode fetch de StationListFragment
}
