// Generated by the protocol buffer compiler. DO NOT EDIT!
// source: trainstation/v1alpha1/station.proto

// Generated files should ignore deprecation warnings
@file:Suppress("DEPRECATION")
package com.example.trainstationapp.data.grpc.trainstation.v1alpha1;

@kotlin.jvm.JvmName("-initializestation")
public inline fun station(block: com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationKt.Dsl.() -> kotlin.Unit): com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.Station =
  com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationKt.Dsl._create(com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.Station.newBuilder()).apply { block() }._build()
/**
 * Protobuf type `trainstation.v1alpha1.Station`
 */
public object StationKt {
  @kotlin.OptIn(com.google.protobuf.kotlin.OnlyForUseByGeneratedProtoCode::class)
  @com.google.protobuf.kotlin.ProtoDslMarker
  public class Dsl private constructor(
    private val _builder: com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.Station.Builder
  ) {
    public companion object {
      @kotlin.jvm.JvmSynthetic
      @kotlin.PublishedApi
      internal fun _create(builder: com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.Station.Builder): Dsl = Dsl(builder)
    }

    @kotlin.jvm.JvmSynthetic
    @kotlin.PublishedApi
    internal fun _build(): com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.Station = _builder.build()

    /**
     * `string id = 1 [json_name = "id"];`
     */
    public var id: kotlin.String
      @JvmName("getId")
      get() = _builder.getId()
      @JvmName("setId")
      set(value) {
        _builder.setId(value)
      }
    /**
     * `string id = 1 [json_name = "id"];`
     */
    public fun clearId() {
      _builder.clearId()
    }

    /**
     * `string commune = 2 [json_name = "commune"];`
     */
    public var commune: kotlin.String
      @JvmName("getCommune")
      get() = _builder.getCommune()
      @JvmName("setCommune")
      set(value) {
        _builder.setCommune(value)
      }
    /**
     * `string commune = 2 [json_name = "commune"];`
     */
    public fun clearCommune() {
      _builder.clearCommune()
    }

    /**
     * <code>double y_wgs84 = 3 [json_name = "yWgs84"];</code>
     */
    public var yWgs84: kotlin.Double
      @JvmName("getYWgs84")
      get() = _builder.getYWgs84()
      @JvmName("setYWgs84")
      set(value) {
        _builder.setYWgs84(value)
      }
    /**
     * `double y_wgs84 = 3 [json_name = "yWgs84"];`
     */
    public fun clearYWgs84() {
      _builder.clearYWgs84()
    }

    /**
     * <code>double x_wgs84 = 4 [json_name = "xWgs84"];</code>
     */
    public var xWgs84: kotlin.Double
      @JvmName("getXWgs84")
      get() = _builder.getXWgs84()
      @JvmName("setXWgs84")
      set(value) {
        _builder.setXWgs84(value)
      }
    /**
     * `double x_wgs84 = 4 [json_name = "xWgs84"];`
     */
    public fun clearXWgs84() {
      _builder.clearXWgs84()
    }

    /**
     * `string libelle = 5 [json_name = "libelle"];`
     */
    public var libelle: kotlin.String
      @JvmName("getLibelle")
      get() = _builder.getLibelle()
      @JvmName("setLibelle")
      set(value) {
        _builder.setLibelle(value)
      }
    /**
     * `string libelle = 5 [json_name = "libelle"];`
     */
    public fun clearLibelle() {
      _builder.clearLibelle()
    }

    /**
     * `string idgaia = 6 [json_name = "idgaia"];`
     */
    public var idgaia: kotlin.String
      @JvmName("getIdgaia")
      get() = _builder.getIdgaia()
      @JvmName("setIdgaia")
      set(value) {
        _builder.setIdgaia(value)
      }
    /**
     * `string idgaia = 6 [json_name = "idgaia"];`
     */
    public fun clearIdgaia() {
      _builder.clearIdgaia()
    }

    /**
     * `string voyageurs = 7 [json_name = "voyageurs"];`
     */
    public var voyageurs: kotlin.String
      @JvmName("getVoyageurs")
      get() = _builder.getVoyageurs()
      @JvmName("setVoyageurs")
      set(value) {
        _builder.setVoyageurs(value)
      }
    /**
     * `string voyageurs = 7 [json_name = "voyageurs"];`
     */
    public fun clearVoyageurs() {
      _builder.clearVoyageurs()
    }

    /**
     * An uninstantiable, behaviorless type to represent the field in
     * generics.
     */
    @kotlin.OptIn(com.google.protobuf.kotlin.OnlyForUseByGeneratedProtoCode::class)
    public class GeoPoint2DProxy private constructor() : com.google.protobuf.kotlin.DslProxy()
    /**
     * <code>repeated double geo_point_2d = 8 [json_name = "geoPoint2d"];</code>
     */
     public val geoPoint2D: com.google.protobuf.kotlin.DslList<kotlin.Double, GeoPoint2DProxy>
      @kotlin.jvm.JvmSynthetic
      get() = com.google.protobuf.kotlin.DslList(
        _builder.getGeoPoint2DList()
      )
    /**
     * `repeated double geo_point_2d = 8 [json_name = "geoPoint2d"];`
     * @param value The geoPoint2d to add.
     */
    @kotlin.jvm.JvmSynthetic
    @kotlin.jvm.JvmName("addGeoPoint2D")
    public fun com.google.protobuf.kotlin.DslList<kotlin.Double, GeoPoint2DProxy>.add(value: kotlin.Double) {
      _builder.addGeoPoint2D(value)
    }/**
     * `repeated double geo_point_2d = 8 [json_name = "geoPoint2d"];`
     * @param value The geoPoint2d to add.
     */
    @kotlin.jvm.JvmSynthetic
    @kotlin.jvm.JvmName("plusAssignGeoPoint2D")
    @Suppress("NOTHING_TO_INLINE")
    public inline operator fun com.google.protobuf.kotlin.DslList<kotlin.Double, GeoPoint2DProxy>.plusAssign(value: kotlin.Double) {
      add(value)
    }/**
     * `repeated double geo_point_2d = 8 [json_name = "geoPoint2d"];`
     * @param values The geoPoint2d to add.
     */
    @kotlin.jvm.JvmSynthetic
    @kotlin.jvm.JvmName("addAllGeoPoint2D")
    public fun com.google.protobuf.kotlin.DslList<kotlin.Double, GeoPoint2DProxy>.addAll(values: kotlin.collections.Iterable<kotlin.Double>) {
      _builder.addAllGeoPoint2D(values)
    }/**
     * `repeated double geo_point_2d = 8 [json_name = "geoPoint2d"];`
     * @param values The geoPoint2d to add.
     */
    @kotlin.jvm.JvmSynthetic
    @kotlin.jvm.JvmName("plusAssignAllGeoPoint2D")
    @Suppress("NOTHING_TO_INLINE")
    public inline operator fun com.google.protobuf.kotlin.DslList<kotlin.Double, GeoPoint2DProxy>.plusAssign(values: kotlin.collections.Iterable<kotlin.Double>) {
      addAll(values)
    }/**
     * `repeated double geo_point_2d = 8 [json_name = "geoPoint2d"];`
     * @param index The index to set the value at.
     * @param value The geoPoint2d to set.
     */
    @kotlin.jvm.JvmSynthetic
    @kotlin.jvm.JvmName("setGeoPoint2D")
    public operator fun com.google.protobuf.kotlin.DslList<kotlin.Double, GeoPoint2DProxy>.set(index: kotlin.Int, value: kotlin.Double) {
      _builder.setGeoPoint2D(index, value)
    }/**
     * `repeated double geo_point_2d = 8 [json_name = "geoPoint2d"];`
     */
    @kotlin.jvm.JvmSynthetic
    @kotlin.jvm.JvmName("clearGeoPoint2D")
    public fun com.google.protobuf.kotlin.DslList<kotlin.Double, GeoPoint2DProxy>.clear() {
      _builder.clearGeoPoint2D()
    }
    /**
     * `string code_ligne = 9 [json_name = "codeLigne"];`
     */
    public var codeLigne: kotlin.String
      @JvmName("getCodeLigne")
      get() = _builder.getCodeLigne()
      @JvmName("setCodeLigne")
      set(value) {
        _builder.setCodeLigne(value)
      }
    /**
     * `string code_ligne = 9 [json_name = "codeLigne"];`
     */
    public fun clearCodeLigne() {
      _builder.clearCodeLigne()
    }

    /**
     * <code>double x_l93 = 10 [json_name = "xL93"];</code>
     */
    public var xL93: kotlin.Double
      @JvmName("getXL93")
      get() = _builder.getXL93()
      @JvmName("setXL93")
      set(value) {
        _builder.setXL93(value)
      }
    /**
     * `double x_l93 = 10 [json_name = "xL93"];`
     */
    public fun clearXL93() {
      _builder.clearXL93()
    }

    /**
     * An uninstantiable, behaviorless type to represent the field in
     * generics.
     */
    @kotlin.OptIn(com.google.protobuf.kotlin.OnlyForUseByGeneratedProtoCode::class)
    public class CGeoProxy private constructor() : com.google.protobuf.kotlin.DslProxy()
    /**
     * <code>repeated double c_geo = 11 [json_name = "cGeo"];</code>
     */
     public val cGeo: com.google.protobuf.kotlin.DslList<kotlin.Double, CGeoProxy>
      @kotlin.jvm.JvmSynthetic
      get() = com.google.protobuf.kotlin.DslList(
        _builder.getCGeoList()
      )
    /**
     * `repeated double c_geo = 11 [json_name = "cGeo"];`
     * @param value The cGeo to add.
     */
    @kotlin.jvm.JvmSynthetic
    @kotlin.jvm.JvmName("addCGeo")
    public fun com.google.protobuf.kotlin.DslList<kotlin.Double, CGeoProxy>.add(value: kotlin.Double) {
      _builder.addCGeo(value)
    }/**
     * `repeated double c_geo = 11 [json_name = "cGeo"];`
     * @param value The cGeo to add.
     */
    @kotlin.jvm.JvmSynthetic
    @kotlin.jvm.JvmName("plusAssignCGeo")
    @Suppress("NOTHING_TO_INLINE")
    public inline operator fun com.google.protobuf.kotlin.DslList<kotlin.Double, CGeoProxy>.plusAssign(value: kotlin.Double) {
      add(value)
    }/**
     * `repeated double c_geo = 11 [json_name = "cGeo"];`
     * @param values The cGeo to add.
     */
    @kotlin.jvm.JvmSynthetic
    @kotlin.jvm.JvmName("addAllCGeo")
    public fun com.google.protobuf.kotlin.DslList<kotlin.Double, CGeoProxy>.addAll(values: kotlin.collections.Iterable<kotlin.Double>) {
      _builder.addAllCGeo(values)
    }/**
     * `repeated double c_geo = 11 [json_name = "cGeo"];`
     * @param values The cGeo to add.
     */
    @kotlin.jvm.JvmSynthetic
    @kotlin.jvm.JvmName("plusAssignAllCGeo")
    @Suppress("NOTHING_TO_INLINE")
    public inline operator fun com.google.protobuf.kotlin.DslList<kotlin.Double, CGeoProxy>.plusAssign(values: kotlin.collections.Iterable<kotlin.Double>) {
      addAll(values)
    }/**
     * `repeated double c_geo = 11 [json_name = "cGeo"];`
     * @param index The index to set the value at.
     * @param value The cGeo to set.
     */
    @kotlin.jvm.JvmSynthetic
    @kotlin.jvm.JvmName("setCGeo")
    public operator fun com.google.protobuf.kotlin.DslList<kotlin.Double, CGeoProxy>.set(index: kotlin.Int, value: kotlin.Double) {
      _builder.setCGeo(index, value)
    }/**
     * `repeated double c_geo = 11 [json_name = "cGeo"];`
     */
    @kotlin.jvm.JvmSynthetic
    @kotlin.jvm.JvmName("clearCGeo")
    public fun com.google.protobuf.kotlin.DslList<kotlin.Double, CGeoProxy>.clear() {
      _builder.clearCGeo()
    }
    /**
     * <code>int64 rg_troncon = 12 [json_name = "rgTroncon", jstype = JS_NUMBER];</code>
     */
    public var rgTroncon: kotlin.Long
      @JvmName("getRgTroncon")
      get() = _builder.getRgTroncon()
      @JvmName("setRgTroncon")
      set(value) {
        _builder.setRgTroncon(value)
      }
    /**
     * `int64 rg_troncon = 12 [json_name = "rgTroncon", jstype = JS_NUMBER];`
     */
    public fun clearRgTroncon() {
      _builder.clearRgTroncon()
    }

    /**
     * `.trainstation.v1alpha1.Geometry geo_shape = 13 [json_name = "geoShape"];`
     */
    public var geoShape: com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.Geometry
      @JvmName("getGeoShape")
      get() = _builder.getGeoShape()
      @JvmName("setGeoShape")
      set(value) {
        _builder.setGeoShape(value)
      }
    /**
     * `.trainstation.v1alpha1.Geometry geo_shape = 13 [json_name = "geoShape"];`
     */
    public fun clearGeoShape() {
      _builder.clearGeoShape()
    }
    /**
     * `.trainstation.v1alpha1.Geometry geo_shape = 13 [json_name = "geoShape"];`
     * @return Whether the geoShape field is set.
     */
    public fun hasGeoShape(): kotlin.Boolean {
      return _builder.hasGeoShape()
    }

    /**
     * `string pk = 14 [json_name = "pk"];`
     */
    public var pk: kotlin.String
      @JvmName("getPk")
      get() = _builder.getPk()
      @JvmName("setPk")
      set(value) {
        _builder.setPk(value)
      }
    /**
     * `string pk = 14 [json_name = "pk"];`
     */
    public fun clearPk() {
      _builder.clearPk()
    }

    /**
     * <code>int64 idreseau = 15 [json_name = "idreseau", jstype = JS_NUMBER];</code>
     */
    public var idreseau: kotlin.Long
      @JvmName("getIdreseau")
      get() = _builder.getIdreseau()
      @JvmName("setIdreseau")
      set(value) {
        _builder.setIdreseau(value)
      }
    /**
     * `int64 idreseau = 15 [json_name = "idreseau", jstype = JS_NUMBER];`
     */
    public fun clearIdreseau() {
      _builder.clearIdreseau()
    }

    /**
     * `string departemen = 16 [json_name = "departemen"];`
     */
    public var departemen: kotlin.String
      @JvmName("getDepartemen")
      get() = _builder.getDepartemen()
      @JvmName("setDepartemen")
      set(value) {
        _builder.setDepartemen(value)
      }
    /**
     * `string departemen = 16 [json_name = "departemen"];`
     */
    public fun clearDepartemen() {
      _builder.clearDepartemen()
    }

    /**
     * <code>double y_l93 = 17 [json_name = "yL93"];</code>
     */
    public var yL93: kotlin.Double
      @JvmName("getYL93")
      get() = _builder.getYL93()
      @JvmName("setYL93")
      set(value) {
        _builder.setYL93(value)
      }
    /**
     * `double y_l93 = 17 [json_name = "yL93"];`
     */
    public fun clearYL93() {
      _builder.clearYL93()
    }

    /**
     * `string fret = 18 [json_name = "fret"];`
     */
    public var fret: kotlin.String
      @JvmName("getFret")
      get() = _builder.getFret()
      @JvmName("setFret")
      set(value) {
        _builder.setFret(value)
      }
    /**
     * `string fret = 18 [json_name = "fret"];`
     */
    public fun clearFret() {
      _builder.clearFret()
    }

    /**
     * <code>bool is_favorite = 19 [json_name = "isFavorite"];</code>
     */
    public var isFavorite: kotlin.Boolean
      @JvmName("getIsFavorite")
      get() = _builder.getIsFavorite()
      @JvmName("setIsFavorite")
      set(value) {
        _builder.setIsFavorite(value)
      }
    /**
     * `bool is_favorite = 19 [json_name = "isFavorite"];`
     */
    public fun clearIsFavorite() {
      _builder.clearIsFavorite()
    }
  }
}
public inline fun com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.Station.copy(block: com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationKt.Dsl.() -> kotlin.Unit): com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.Station =
  com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationKt.Dsl._create(this.toBuilder()).apply { block() }._build()

public val com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.StationOrBuilder.geoShapeOrNull: com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.Geometry?
  get() = if (hasGeoShape()) getGeoShape() else null

