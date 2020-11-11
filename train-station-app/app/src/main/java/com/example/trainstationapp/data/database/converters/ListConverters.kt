package com.example.trainstationapp.data.database.converters

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ListConverters {
    @TypeConverter
    fun fromString(value: String): List<Double> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromList(list: List<Double>): String {
        return Json.encodeToString(list)
    }
}