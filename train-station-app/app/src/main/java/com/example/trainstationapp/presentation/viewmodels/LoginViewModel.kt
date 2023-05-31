package com.example.trainstationapp.presentation.viewmodels

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trainstationapp.data.datastore.Session
import com.example.trainstationapp.data.datastore.jwt
import com.example.trainstationapp.data.grpc.auth.v1alpha1.AuthAPIGrpcKt
import com.example.trainstationapp.data.grpc.auth.v1alpha1.account
import com.example.trainstationapp.data.grpc.auth.v1alpha1.getJWTRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class LoginViewModel
@Inject
constructor(
    jwtDataStore: DataStore<Session.Jwt>,
    oauthDataStore: DataStore<Session.OAuth>,
    auth: AuthAPIGrpcKt.AuthAPICoroutineStub
) : ViewModel() {
    init {
        viewModelScope.launch {
            oauthDataStore.data.collect {
                if (it.accessToken == "") return@collect
                try {
                    // Fetch session token
                    val resp =
                        auth.getJWT(
                            getJWTRequest {
                                account = account {
                                    provider = "github"
                                    type = "oauth"
                                    accessToken = it.accessToken
                                    providerAccountId = it.userId.toString()
                                }
                            }
                        )

                    // Store the session token
                    jwtDataStore.updateData { jwt { this.token = resp.token } }
                } catch (e: Exception) {
                    // OAuth token has failed, so, clean the oauth token
                    Timber.e(e)
                    _errorState.value = e.toString()
                    oauthDataStore.updateData { oauth -> oauth.defaultInstanceForType }
                }
            }
        }
    }

    private val _errorState = MutableStateFlow<String?>(null)
    val error: StateFlow<String?>
        get() = _errorState

    fun clearError() {
        _errorState.value = null
    }

    val isOnline: StateFlow<Boolean> =
        jwtDataStore.data
            .map { !it.token.isNullOrEmpty() }
            .catch { e -> _errorState.value = e.toString() }
            .stateIn(viewModelScope, SharingStarted.Lazily, false)
}
