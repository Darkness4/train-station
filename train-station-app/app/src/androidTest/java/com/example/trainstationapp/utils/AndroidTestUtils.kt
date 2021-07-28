package com.example.trainstationapp.utils

import com.example.trainstationapp.data.models.StationModel

object AndroidTestUtils {
    fun createStationModel(id: String) =
        StationModel(
            recordid = id,
            datasetid = "datasetid",
            isFavorite = false,
            libelle = "libelle",
            fields =
            StationModel.FieldsModel(
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
                geoShape =
                StationModel.GeometryModel(
                    type = "type",
                    coordinates = listOf(1.0),
                ),
                pk = "pk",
                idreseau = 1,
                departemen = "departemen",
                yL93 = 1.0,
                fret = "fret",
            ),
            geometry =
            StationModel.GeometryModel(
                type = "type",
                coordinates = listOf(1.0),
            ),
            recordTimestamp = "10",
        )
}
