package com.example.trainstationapp.domain.entities

import com.example.trainstationapp.core.mappers.ModelMappable
import com.example.trainstationapp.data.models.StationModel

data class Station(
    val recordid: String,
    val datasetid: String,
    val isFavorite: Boolean,
    val fields: Fields,
    val geometry: Geometry,
    val recordTimestamp: String,
) : ModelMappable<StationModel> {
    data class Fields(
        val id: Int,
        val commune: String,
        val yWgs84: Double,
        val xWgs84: Double,
        val libelle: String,
        val idgaia: String,
        val voyageurs: String,
        val geoPoint2d: List<Double>,
        val codeLigne: String,
        val xL93: Double,
        val cGeo: List<Double>,
        val rgTroncon: Int,
        val geoShape: Geometry,
        val pk: String,
        val idreseau: Int,
        val departemen: String,
        val yL93: Double,
        val fret: String,
    ): ModelMappable<StationModel.FieldsModel> {
        override fun asModel()= StationModel.FieldsModel(
            id,
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
            geoShape.asModel(),
            pk,
            idreseau,
            departemen,
            yL93,
            fret,
        )
    }

    data class Geometry(
        val id: String,
        val type: String,
        val coordinates: List<Double>,
    ): ModelMappable<StationModel.GeometryModel> {
        override fun asModel() = StationModel.GeometryModel(
            id,
            type,
            coordinates,
        )
    }

    override fun asModel() = StationModel(
        recordid,
        datasetid,
        isFavorite,
        fields.asModel(),
        geometry.asModel(),
        recordTimestamp
    )
}
