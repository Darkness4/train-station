package com.example.trainstationapp.data.oidc

import com.nimbusds.jwt.JWTClaimsSet

data class OidcIdTokenClaims(
    val subject: String,
    val name: String,
)

fun JWTClaimsSet.toOidcIdTokenClaims() = OidcIdTokenClaims(
    subject = subject,
    name = getStringClaim("name"),
)
