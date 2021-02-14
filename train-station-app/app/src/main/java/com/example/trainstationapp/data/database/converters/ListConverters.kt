package com.example.trainstationapp.data.database.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

/**
 * Converters for `List` to `String` to be able to store `List` in `Room`.
 */
@ProvidedTypeConverter
class ListConverters private constructor(private val listOfDoubleAdapter: JsonAdapter<List<Double>>) {
    @TypeConverter
    fun fromString(value: String): List<Double> {
        return listOfDoubleAdapter.fromJson(value)!!
    }

    @TypeConverter
    fun fromList(list: List<Double>): String {
        return listOfDoubleAdapter.toJson(list)
    }

    companion object {
        fun create(moshi: Moshi): ListConverters {
            val listOfDoubleAdapter: JsonAdapter<List<Double>> = moshi.adapter(
                Types.newParameterizedType(
                    List::class.java,
                    Double::class.javaObjectType
                )
            )

            return ListConverters(listOfDoubleAdapter)
        }
    }
}
