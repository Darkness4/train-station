package com.example.trainstationapp.presentation.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trainstationapp.data.datastore.Session
import com.example.trainstationapp.data.datastore.codeVerifier
import com.example.trainstationapp.data.datastore.oAuth
import com.example.trainstationapp.data.oidc.OidcClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds
import kotlin.times

@HiltViewModel
class LoginViewModel
@Inject
constructor(
    private val oauthDataStore: DataStore<Session.OAuth>,
    private val codeVerifierDataStore: DataStore<Session.CodeVerifier>,
    private val oidcClient: OidcClient,
) : ViewModel() {
    companion object {
        private val REFRESH_THRESHOLD = 5 * 60 * 1000
    }

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

    // Ticker checks if token has expired
    private val ticker = flow {
        while (true) {
            emit(System.currentTimeMillis())
            delay(10.seconds)
        }
    }

    val isOnline: StateFlow<Boolean> = oauthDataStore.data
        .combine(ticker) { data, now ->
            val token = data.accessToken
            val refreshToken = data.refreshToken
            val expiresAt = data.expiresAt

            if (!token.isNullOrEmpty() && !refreshToken.isNullOrEmpty()) {
                val timeRemaining = expiresAt - now

                if (timeRemaining in 1..REFRESH_THRESHOLD) {
                    launchRefreshToken(refreshToken)
                }
            }

            !token.isNullOrEmpty() && expiresAt > now
        }
        .catch { e ->
            Timber.e(e)
            _error.value = e.toString()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false,
        )

    private var isRefreshing = mutableStateOf(false)

    private fun launchRefreshToken(refreshToken: String) {
        if (isRefreshing.value) return

        viewModelScope.launch {
            Timber.i("Refreshing access token")
            isRefreshing.value = true
            try {
                val token = oidcClient.refreshToken(refreshToken)
                val idToken = oidcClient.parseIdToken(token.idToken)
                oauthDataStore.updateData {
                    oAuth {
                        accessToken = token.accessToken
                        this.refreshToken = token.refreshToken
                        expiresAt = System.currentTimeMillis() + (token.expiresIn * 1000L)
                        username = idToken.name
                    }
                }
            } catch (e: Exception) {
                Timber.e("Refresh failed: ${e.message}")
            } finally {
                isRefreshing.value = false
            }
        }
    }

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
