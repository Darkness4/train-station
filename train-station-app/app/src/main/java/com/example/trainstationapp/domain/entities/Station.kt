package com.example.trainstationapp.domain.entities

data class Station(
    val recordid: String,
    val datasetid: String,
    val isFavorite: Boolean,
    val fields: Fields,
    val geometry: Geometry,
    val recordTimestamp: String,
) {
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
    )

    data class Geometry(
        val type: String,
        val coordinates: List<Double>,
    )
}
