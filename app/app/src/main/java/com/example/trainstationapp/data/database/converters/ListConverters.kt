package com.example.trainstationapp.data.database.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/** Converters for `List` to `String` to be able to store `List` in `Room`. */
@ProvidedTypeConverter
class ListConverters private constructor(private val json: Json) {
    @TypeConverter
    fun fromString(value: String): List<Double> {
        return json.decodeFromString(value)
    }

    @TypeConverter
    fun fromList(list: List<Double>): String {
        return json.encodeToString(list)
    }

    companion object {
        fun create(json: Json): ListConverters {
            return ListConverters(json)
        }
    }
}
