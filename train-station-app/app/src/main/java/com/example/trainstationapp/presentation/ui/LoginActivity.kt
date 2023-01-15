package com.example.trainstationapp.presentation.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.lifecycle.coroutineScope
import com.example.trainstationapp.R
import com.example.trainstationapp.data.datastore.JwtOuterClass.Jwt
import com.example.trainstationapp.data.datastore.jwt
import com.example.trainstationapp.data.github.GithubApi
import com.example.trainstationapp.data.grpc.auth.v1alpha1.AuthAPIGrpcKt
import com.example.trainstationapp.data.grpc.auth.v1alpha1.account
import com.example.trainstationapp.data.grpc.auth.v1alpha1.getJWTRequest
import com.example.trainstationapp.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ClientSecretBasic
import net.openid.appauth.ResponseTypeValues
import timber.log.Timber

private object GithubAuth {
    val redirectUri: Uri = Uri.parse("https://train.the-end-is-never-the-end.pw")
    val authorizeUri: Uri = Uri.parse("https://github.com/login/oauth/authorize")
    val tokenUri: Uri = Uri.parse("https://github.com/login/oauth/access_token")
}

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    @Inject lateinit var service: AuthorizationService
    @Inject lateinit var jwtDataStore: DataStore<Jwt>
    @Inject lateinit var githubApi: GithubApi
    @Inject lateinit var auth: AuthAPIGrpcKt.AuthAPICoroutineStub

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this

        setContentView(binding.root)
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { it ->
            if (it.resultCode == RESULT_OK) {
                val ex = AuthorizationException.fromIntent(it.data!!)
                val result = AuthorizationResponse.fromIntent(it.data!!)

                if (ex != null) {
                    Timber.e("Github Auth", "launcher: $ex")
                } else {
                    val secret = ClientSecretBasic(getString(R.string.github_client_secret))
                    val tokenRequest = result?.createTokenExchangeRequest()

                    service.performTokenRequest(tokenRequest!!, secret) { res, exception ->
                        if (exception != null) {
                            Timber.e("Github Auth", "launcher: ${exception.error}")
                        } else {
                            lifecycle.coroutineScope.launch {
                                val token = res?.accessToken!!
                                val user = githubApi.user("Bearer $token")

                                val resp =
                                    auth.getJWT(
                                        getJWTRequest {
                                            account = account {
                                                provider = "github"
                                                type = "oauth"
                                                accessToken = token
                                                providerAccountId = user.id.toString()
                                            }
                                        }
                                    )
                                jwtDataStore.updateData { jwt { this.token = resp.token } }
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                finish()
                            }
                        }
                    }
                }
            }
        }

    private fun githubAuth() {
        val config = AuthorizationServiceConfiguration(GithubAuth.authorizeUri, GithubAuth.tokenUri)

        val request =
            AuthorizationRequest.Builder(
                    config,
                    getString(R.string.github_client_id),
                    ResponseTypeValues.CODE,
                    GithubAuth.redirectUri
                )
                .setScopes("read:user user:email")
                .build()

        val intent = service.getAuthorizationRequestIntent(request)
        launcher.launch(intent)
    }
}
