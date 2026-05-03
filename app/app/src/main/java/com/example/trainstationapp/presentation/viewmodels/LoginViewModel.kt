package com.example.trainstationapp.presentation.viewmodels

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trainstationapp.data.datastore.Session
import com.example.trainstationapp.data.datastore.codeVerifier
import com.example.trainstationapp.data.datastore.oAuth
import com.example.trainstationapp.data.oidc.OidcClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.times

@HiltViewModel
class LoginViewModel
@Inject
constructor(
    private val oauthDataStore: DataStore<Session.OAuth>,
    private val codeVerifierDataStore: DataStore<Session.CodeVerifier>,
    private val oidcClient: OidcClient,
) : ViewModel() {
    private var _code = MutableStateFlow<String?>(null)
    var code: String?
        get() = _code.value
        set(code) {
            _code.value = code
        }

    private val codeVerifier: StateFlow<Session.CodeVerifier?> = codeVerifierDataStore.data.stateIn(viewModelScope, SharingStarted.Lazily, null)

    init {
        watchCallback()
    }

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?>
        get() = _error

    fun clearError() {
        _error.value = null
    }

    val isOnline: StateFlow<Boolean> =
        oauthDataStore.data
            .map { !it.accessToken.isNullOrEmpty() }
            .catch { e ->
                Timber.e(e)
                _error.value = e.toString()
            }
            .stateIn(viewModelScope, SharingStarted.Lazily, false)

    fun provideAuthorizationUrl(): String {
        val verifier = OidcClient.generateCodeVerifier()
        viewModelScope.launch {
            codeVerifierDataStore.updateData {
                codeVerifier {
                    value = verifier
                }
            }
        }
        return oidcClient.getAuthorizationUrl(verifier)
    }

    fun watchCallback() {
        _code.combine(codeVerifier) { code, verifier ->
            if (code != null && verifier != null) code to verifier else null
        }.filterNotNull().onEach { (code, verifier) ->
            val token = oidcClient.fetchAccessToken(code, verifier.value)
            val idToken = oidcClient.parseIdToken(token.idToken)
            oauthDataStore.updateData {
                oAuth {
                    accessToken = token.accessToken
                    refreshToken = token.refreshToken
                    expiresAt = System.currentTimeMillis() + (token.expiresIn * 1000L)
                    username = idToken.name
                }
            }
        }.launchIn(viewModelScope)
    }
}
