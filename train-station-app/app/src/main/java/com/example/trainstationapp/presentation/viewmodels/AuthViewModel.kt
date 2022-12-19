package com.example.trainstationapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel(private val firebaseAuth: FirebaseAuth) : ViewModel() {
    private val idTokenListener =
        FirebaseAuth.IdTokenListener {
            it.currentUser?.getIdToken(false)?.addOnSuccessListener { result ->
                _idToken.value = result.token
            }
        }

    private val _idToken = MutableStateFlow<String?>(null)
    val idToken: StateFlow<String?>
        get() = _idToken

    init {
        firebaseAuth.addIdTokenListener(idTokenListener)
    }

    companion object {
        fun provideFactory(firebaseAuth: FirebaseAuth): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AuthViewModel(firebaseAuth) as T
                }
            }
    }

    override fun onCleared() {
        firebaseAuth.removeIdTokenListener(idTokenListener)
        super.onCleared()
    }
}
