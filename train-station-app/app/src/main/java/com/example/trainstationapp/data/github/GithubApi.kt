package com.example.trainstationapp.data.github

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface GithubApi {
    companion object {
        const val BASE_URL = "https://api.github.com/"
        const val CONTENT_TYPE = "application/vnd.github+json"
    }
    @Headers("X-GitHub-Api-Version: 2022-11-28", "Accept: application/vnd.github+json")
    @GET("user")
    suspend fun user(@Header("Authorization") authorization: String): User
}
