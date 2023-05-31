package com.example.trainstationapp.domain.entities

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "stations")
data class Station(
    @PrimaryKey val id: String = "",
    @ColumnInfo(name = "is_favorite") var isFavorite: Boolean = false,
    val commune: String = "",
    @ColumnInfo(name = "y_wgs84") val yWgs84: Double = 0.0,
    @ColumnInfo(name = "x_wgs84") val xWgs84: Double = 0.0,
    val libelle: String = "",
    val idgaia: String = "",
    val voyageurs: String = "",
    @ColumnInfo(name = "geo_point_2d") val geoPoint2d: List<Double> = emptyList(),
    @ColumnInfo(name = "code_ligne") val codeLigne: String = "",
    @ColumnInfo(name = "x_l93") val xL93: Double = 0.0,
    @ColumnInfo(name = "c_geo") val cGeo: List<Double> = emptyList(),
    @ColumnInfo(name = "rg_troncon") val rgTroncon: Long = 0,
    @Embedded(prefix = "geo_shape_") val geoShape: Geometry = Geometry(),
    val pk: String = "",
    val idreseau: Long = 0,
    val departemen: String = "",
    @ColumnInfo(name = "y_l93") val yL93: Double = 0.0,
    val fret: String = ""
) : Parcelable {

    fun toggleFavorite() = apply { isFavorite = !isFavorite }

    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Geometry(
        val type: String = "",
        val coordinates: List<Double> = emptyList(),
    ) : Parcelable {
        companion object {
            fun fromGrpc(model: StationProto.Geometry) =
                Geometry(
                    model.type,
                    model.coordinatesList,
                )
        }

        fun asGrpcModel() =
            StationProto.Geometry.newBuilder().setType(type).addAllCoordinates(coordinates).build()
    }

    companion object {
        fun fromGrpc(model: StationProto.Station) =
            Station(
                model.id,
                model.isFavorite,
                model.commune,
                model.yWgs84,
                model.xWgs84,
                model.libelle,
                model.idgaia,
                model.voyageurs,
                model.geoPoint2DList,
                model.codeLigne,
                model.xL93,
                model.cGeoList,
                model.rgTroncon,
                Geometry.fromGrpc(model.geoShape),
                model.pk,
                model.idreseau,
                model.departemen,
                model.yL93,
                model.fret,
            )
    }

    fun asGrpcModel(): StationProto.Station =
        StationProto.Station.newBuilder()
            .setId(id)
            .setIsFavorite(isFavorite)
            .setCommune(commune)
            .setYWgs84(yWgs84)
            .setXWgs84(xWgs84)
            .setLibelle(libelle)
            .setIdgaia(idgaia)
            .setVoyageurs(voyageurs)
            .addAllGeoPoint2D(geoPoint2d)
            .setCodeLigne(codeLigne)
            .setXL93(xL93)
            .addAllCGeo(cGeo)
            .setRgTroncon(rgTroncon)
            .setGeoShape(geoShape.asGrpcModel())
            .setPk(pk)
            .setIdreseau(idreseau)
            .setDepartemen(departemen)
            .setYL93(yL93)
            .setFret(fret)
            .build()
}
