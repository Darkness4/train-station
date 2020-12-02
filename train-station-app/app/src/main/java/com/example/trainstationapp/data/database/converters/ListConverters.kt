package com.example.trainstationapp.data.database.converters

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.Type

/**
 * Converters for `List` to `String` to be able to store `List` in `Room`.
 */
class ListConverters {
    private val moshi = Moshi.Builder().build()
    private val listOfDouble: Type = Types.newParameterizedType(
        List::class.java,
        Double::class.javaObjectType
    )
    private val adapter: JsonAdapter<List<Double>> = moshi.adapter(listOfDouble)

    @TypeConverter
    fun fromString(value: String): List<Double> {
        return adapter.fromJson(value)!!
    }

    @TypeConverter
    fun fromList(list: List<Double>): String {
        return adapter.toJson(list)
    }
}
