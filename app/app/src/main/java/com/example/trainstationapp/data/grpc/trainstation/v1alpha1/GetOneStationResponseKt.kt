// Generated by the protocol buffer compiler. DO NOT EDIT!
// source: trainstation/v1alpha1/station.proto

// Generated files should ignore deprecation warnings
@file:Suppress("DEPRECATION")
package com.example.trainstationapp.data.grpc.trainstation.v1alpha1;

@kotlin.jvm.JvmName("-initializegetOneStationResponse")
public inline fun getOneStationResponse(block: com.example.trainstationapp.data.grpc.trainstation.v1alpha1.GetOneStationResponseKt.Dsl.() -> kotlin.Unit): com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetOneStationResponse =
  com.example.trainstationapp.data.grpc.trainstation.v1alpha1.GetOneStationResponseKt.Dsl._create(com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetOneStationResponse.newBuilder()).apply { block() }._build()
/**
 * Protobuf type `trainstation.v1alpha1.GetOneStationResponse`
 */
public object GetOneStationResponseKt {
  @kotlin.OptIn(com.google.protobuf.kotlin.OnlyForUseByGeneratedProtoCode::class)
  @com.google.protobuf.kotlin.ProtoDslMarker
  public class Dsl private constructor(
    private val _builder: com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetOneStationResponse.Builder
  ) {
    public companion object {
      @kotlin.jvm.JvmSynthetic
      @kotlin.PublishedApi
      internal fun _create(builder: com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetOneStationResponse.Builder): Dsl = Dsl(builder)
    }

    @kotlin.jvm.JvmSynthetic
    @kotlin.PublishedApi
    internal fun _build(): com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetOneStationResponse = _builder.build()

    /**
     * `.trainstation.v1alpha1.Station station = 1 [json_name = "station"];`
     */
    public var station: com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.Station
      @JvmName("getStation")
      get() = _builder.getStation()
      @JvmName("setStation")
      set(value) {
        _builder.setStation(value)
      }
    /**
     * `.trainstation.v1alpha1.Station station = 1 [json_name = "station"];`
     */
    public fun clearStation() {
      _builder.clearStation()
    }
    /**
     * `.trainstation.v1alpha1.Station station = 1 [json_name = "station"];`
     * @return Whether the station field is set.
     */
    public fun hasStation(): kotlin.Boolean {
      return _builder.hasStation()
    }
  }
}
public inline fun com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetOneStationResponse.copy(block: com.example.trainstationapp.data.grpc.trainstation.v1alpha1.GetOneStationResponseKt.Dsl.() -> kotlin.Unit): com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetOneStationResponse =
  com.example.trainstationapp.data.grpc.trainstation.v1alpha1.GetOneStationResponseKt.Dsl._create(this.toBuilder()).apply { block() }._build()

public val com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetOneStationResponseOrBuilder.stationOrNull: com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.Station?
  get() = if (hasStation()) getStation() else null

