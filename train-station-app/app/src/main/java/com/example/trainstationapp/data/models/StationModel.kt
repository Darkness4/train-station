package com.example.trainstationapp.data.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.trainstationapp.domain.entities.Station
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = "stations")
@Serializable
data class StationModel(
    @PrimaryKey val recordid: String,
    val datasetid: String,
    @ColumnInfo(name = "is_favorite") @SerialName("is_favorite")
    val isFavorite: Boolean,
    val libelle: String,
    @Embedded(prefix = "fields_") val fields: FieldsModel? = null,
    @Embedded(prefix = "geometry_") val geometry: GeometryModel? = null,
    @ColumnInfo(name = "record_timestamp")
    @SerialName("record_timestamp")
    val recordTimestamp: String
) {

    @Serializable
    data class FieldsModel(
        val commune: String,
        @ColumnInfo(name = "y_wgs84") @SerialName("y_wgs84")
        val yWgs84: Double,
        @ColumnInfo(name = "x_wgs84") @SerialName("x_wgs84")
        val xWgs84: Double,
        val libelle: String,
        val idgaia: String,
        val voyageurs: String,
        @ColumnInfo(name = "geo_point_2d") @SerialName("geo_point_2d")
        val geoPoint2d: List<Double>,
        @ColumnInfo(name = "code_ligne") @SerialName("code_ligne")
        val codeLigne: String,
        @ColumnInfo(name = "x_l93") @SerialName("x_l93")
        val xL93: Double,
        @ColumnInfo(name = "c_geo") @SerialName("c_geo")
        val cGeo: List<Double>,
        @ColumnInfo(name = "rg_troncon") @SerialName("rg_troncon")
        val rgTroncon: Int,
        @Embedded(prefix = "geo_shape_") @SerialName("geo_shape")
        val geoShape: GeometryModel,
        val pk: String,
        val idreseau: Int,
        val departemen: String,
        @ColumnInfo(name = "y_l93") @SerialName("y_l93")
        val yL93: Double,
        val fret: String
    ) {
        fun asEntity() =
            Station.Fields(
                commune,
                yWgs84,
                xWgs84,
                libelle,
                idgaia,
                voyageurs,
                geoPoint2d,
                codeLigne,
                xL93,
                cGeo,
                rgTroncon,
                geoShape.asEntity(),
                pk,
                idreseau,
                departemen,
                yL93,
                fret
            )
    }

    @Serializable
    data class GeometryModel(
        val type: String,
        val coordinates: List<Double>
    ) {
        fun asEntity() =
            Station.Geometry(
                type,
                coordinates
            )
    }

    fun asEntity() =
        Station(
            recordid,
            datasetid,
            isFavorite,
            libelle,
            fields?.asEntity(),
            geometry?.asEntity(),
            recordTimestamp
        )
}
