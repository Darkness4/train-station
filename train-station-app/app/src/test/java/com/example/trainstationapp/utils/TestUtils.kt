package com.example.trainstationapp.utils

import com.example.trainstationapp.domain.entities.Station

object TestUtils {
    fun createStation(id: String) =
        Station(
            id = id,
            isFavorite = false,
            commune = "commune",
            yWgs84 = 1.0,
            xWgs84 = 1.0,
            libelle = "libelle",
            idgaia = "idgaia",
            voyageurs = "voyageurs",
            geoPoint2d = listOf(1.0),
            codeLigne = "codeLigne",
            xL93 = 1.0,
            cGeo = listOf(1.0),
            rgTroncon = 1,
            geoShape = Station.Geometry(type = "type", coordinates = listOf(1.0)),
            pk = "pk",
            idreseau = 1,
            departemen = "departemen",
            yL93 = 1.0,
            fret = "fret"
        )
}
