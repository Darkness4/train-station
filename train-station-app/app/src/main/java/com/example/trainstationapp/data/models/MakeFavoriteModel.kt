package com.example.trainstationapp.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable data class MakeFavoriteModel(@SerialName("is_favorite") val isFavorite: Boolean)
