package com.example.trainstationapp.data.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.trainstationapp.core.mappers.EntityMappable
import com.example.trainstationapp.domain.entities.Station
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(tableName = "stations")
@JsonClass(generateAdapter = true)
data class StationModel(
    @PrimaryKey
    val recordid: String,
    val datasetid: String,
    @ColumnInfo(name = "is_favorite") @field:Json(name = "is_favorite") val isFavorite: Boolean,
    val libelle: String,
    @Embedded(prefix = "fields_") val fields: FieldsModel? = null,
    @Embedded(prefix = "geometry_") val geometry: GeometryModel? = null,
    @ColumnInfo(name = "record_timestamp") @field:Json(name = "record_timestamp") val recordTimestamp: String,
) : EntityMappable<Station> {

    @JsonClass(generateAdapter = true)
    data class FieldsModel(
        val commune: String,
        @ColumnInfo(name = "y_wgs84") @field:Json(name = "y_wgs84") val yWgs84: Double,
        @ColumnInfo(name = "x_wgs84") @field:Json(name = "x_wgs84") val xWgs84: Double,
        val libelle: String,
        val idgaia: String,
        val voyageurs: String,
        @ColumnInfo(name = "geo_point_2d") @field:Json(name = "geo_point_2d") val geoPoint2d: List<Double>,
        @ColumnInfo(name = "code_ligne") @field:Json(name = "code_ligne") val codeLigne: String,
        @ColumnInfo(name = "x_l93") @field:Json(name = "x_l93") val xL93: Double,
        @ColumnInfo(name = "c_geo") @field:Json(name = "c_geo") val cGeo: List<Double>,
        @ColumnInfo(name = "rg_troncon") @field:Json(name = "rg_troncon") val rgTroncon: Int,
        @Embedded(prefix = "geo_shape_") @field:Json(name = "geo_shape") val geoShape: GeometryModel,
        val pk: String,
        val idreseau: Int,
        val departemen: String,
        @ColumnInfo(name = "y_l93") @field:Json(name = "y_l93") val yL93: Double,
        val fret: String,
    ) : EntityMappable<Station.Fields> {
        override fun asEntity() = Station.Fields(
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
            fret,
        )
    }

    @JsonClass(generateAdapter = true)
    data class GeometryModel(
        val type: String,
        val coordinates: List<Double>,
    ) : EntityMappable<Station.Geometry> {
        override fun asEntity() = Station.Geometry(
            type,
            coordinates,
        )
    }

    override fun asEntity() = Station(
        recordid,
        datasetid,
        isFavorite,
        libelle,
        fields?.asEntity(),
        geometry?.asEntity(),
        recordTimestamp
    )
}
