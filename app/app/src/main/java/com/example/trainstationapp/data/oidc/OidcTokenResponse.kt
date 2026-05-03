package com.example.trainstationapp.data.oidc

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OidcTokenResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("expires_in") val expiresIn: Int,
    @SerialName("refresh_token") val refreshToken: String,
    @SerialName("id_token") val idToken: String,
)
