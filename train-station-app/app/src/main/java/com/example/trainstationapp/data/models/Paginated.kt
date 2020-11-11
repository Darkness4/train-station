package com.example.trainstationapp.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Paginated<out T>(
    val data: List<T>,
    val count: Int,
    val total: Int,
    val page: Int,
    val pageCount: Int,
)