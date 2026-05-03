package com.example.trainstationapp.data.oidc

import android.util.Base64
import androidx.core.net.toUri
import com.example.trainstationapp.BuildConfig
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.jwk.source.JWKSourceBuilder
import com.nimbusds.jose.proc.JWSVerificationKeySelector
import com.nimbusds.jose.proc.SecurityContext
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.proc.DefaultJWTClaimsVerifier
import com.nimbusds.jwt.proc.DefaultJWTProcessor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URL
import java.security.MessageDigest
import java.security.SecureRandom
import javax.inject.Inject

class OidcClient @Inject constructor(
    private val httpClient: OkHttpClient,
    private val json: Json,
    private val discovery: OidcDiscoveryDocument,
    private val scopes: List<String> = listOf("openid", "profile", "offline_access"),
) {
    private val jwkSource = JWKSourceBuilder
        .create<SecurityContext>(URL(discovery.jwksUri))
        .cache(true)
        .rateLimited(true)
        .build()

    companion object {
        const val REDIRECT_URI = "com.example.trainstationapp://oauth2"

        fun generateCodeVerifier(): String {
            val secureRandom = SecureRandom()
            val code = ByteArray(64)
            secureRandom.nextBytes(code)
            return Base64.encodeToString(code, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
        }

        private fun generateCodeChallenge(codeVerifier: String): String {
            val bytes = codeVerifier.toByteArray(Charsets.US_ASCII)
            val digest = MessageDigest.getInstance("SHA-256").digest(bytes)
            return Base64.encodeToString(digest, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
        }
    }

    fun getAuthorizationUrl(
        codeVerifier: String,
    ): String = discovery.authorizationEndpoint.toUri().buildUpon()
        .appendQueryParameter("response_type", "code")
        .appendQueryParameter("client_id", BuildConfig.CLIENT_ID)
        .appendQueryParameter("redirect_uri", REDIRECT_URI)
        .appendQueryParameter("scope", scopes.joinToString(" "))
        .appendQueryParameter("code_challenge", generateCodeChallenge(codeVerifier))
        .appendQueryParameter("code_challenge_method", "S256")
        .build()
        .toString()

    suspend fun fetchAccessToken(
        code: String,
        codeVerifier: String,
    ): OidcTokenResponse = withContext(Dispatchers.IO) {
        val body = FormBody.Builder()
            .add("grant_type", "authorization_code")
            .add("client_id", BuildConfig.CLIENT_ID)
            .add("redirect_uri", REDIRECT_URI)
            .add("code", code)
            .add("code_verifier", codeVerifier)
            .build()

        executeTokenRequest(
            Request.Builder()
                .url(discovery.tokenEndpoint)
                .post(body)
                .header("Accept", "application/json")
                .build(),
        )
    }

    suspend fun refreshToken(
        refreshToken: String,
    ): OidcTokenResponse = withContext(Dispatchers.IO) {
        val body = FormBody.Builder()
            .add("grant_type", "refresh_token")
            .add("client_id", BuildConfig.CLIENT_ID)
            .add("refresh_token", refreshToken)
            .add("scope", scopes.joinToString(" "))
            .build()

        executeTokenRequest(
            Request.Builder()
                .url(discovery.tokenEndpoint)
                .post(body)
                .header("Accept", "application/json")
                .build(),
        )
    }

    suspend fun parseIdToken(idToken: String): OidcIdTokenClaims = withContext(Dispatchers.IO) {
        val processor = DefaultJWTProcessor<SecurityContext>().apply {
            jwsKeySelector = JWSVerificationKeySelector(
                JWSAlgorithm.RS256,
                jwkSource,
            )

            jwtClaimsSetVerifier = DefaultJWTClaimsVerifier(
                setOf(BuildConfig.CLIENT_ID),
                JWTClaimsSet.Builder()
                    .issuer(discovery.issuer)
                    .build(),
                setOf("sub", "name"),
                emptySet(),
            )
        }

        val claims = try {
            processor.process(idToken, null)
        } catch (e: Exception) {
            throw OidcException("ID token validation failed: ${e.message}", e)
        }

        claims.toOidcIdTokenClaims()
    }

    private fun executeTokenRequest(request: Request): OidcTokenResponse {
        val response = httpClient.newCall(request).execute()
        val body = response.body.string()
        if (!response.isSuccessful) {
            throw OidcException("Token endpoint HTTP ${response.code}: $body")
        }
        return try {
            json.decodeFromString(OidcTokenResponse.serializer(), body)
        } catch (e: Exception) {
            throw OidcException("Failed to parse token response", e)
        }
    }
}
