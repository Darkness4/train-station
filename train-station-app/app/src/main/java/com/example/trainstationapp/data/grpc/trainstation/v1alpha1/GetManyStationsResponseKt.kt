//Generated by the protocol buffer compiler. DO NOT EDIT!
// source: trainstation/v1alpha1/station.proto

package com.example.trainstationapp.data.grpc.trainstation.v1alpha1;

@kotlin.jvm.JvmName("-initializegetManyStationsResponse")
public inline fun getManyStationsResponse(block: com.example.trainstationapp.data.grpc.trainstation.v1alpha1.GetManyStationsResponseKt.Dsl.() -> kotlin.Unit): com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetManyStationsResponse =
  com.example.trainstationapp.data.grpc.trainstation.v1alpha1.GetManyStationsResponseKt.Dsl._create(com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetManyStationsResponse.newBuilder()).apply { block() }._build()
public object GetManyStationsResponseKt {
  @kotlin.OptIn(com.google.protobuf.kotlin.OnlyForUseByGeneratedProtoCode::class)
  @com.google.protobuf.kotlin.ProtoDslMarker
  public class Dsl private constructor(
    private val _builder: com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetManyStationsResponse.Builder
  ) {
    public companion object {
      @kotlin.jvm.JvmSynthetic
      @kotlin.PublishedApi
      internal fun _create(builder: com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetManyStationsResponse.Builder): Dsl = Dsl(builder)
    }

    @kotlin.jvm.JvmSynthetic
    @kotlin.PublishedApi
    internal fun _build(): com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetManyStationsResponse = _builder.build()

    /**
     * <code>.trainstation.v1alpha1.PaginatedStation stations = 1 [json_name = "stations"];</code>
     */
    public var stations: com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.PaginatedStation
      @JvmName("getStations")
      get() = _builder.getStations()
      @JvmName("setStations")
      set(value) {
        _builder.setStations(value)
      }
    /**
     * <code>.trainstation.v1alpha1.PaginatedStation stations = 1 [json_name = "stations"];</code>
     */
    public fun clearStations() {
      _builder.clearStations()
    }
    /**
     * <code>.trainstation.v1alpha1.PaginatedStation stations = 1 [json_name = "stations"];</code>
     * @return Whether the stations field is set.
     */
    public fun hasStations(): kotlin.Boolean {
      return _builder.hasStations()
    }
  }
}
public inline fun com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetManyStationsResponse.copy(block: com.example.trainstationapp.data.grpc.trainstation.v1alpha1.GetManyStationsResponseKt.Dsl.() -> kotlin.Unit): com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetManyStationsResponse =
  com.example.trainstationapp.data.grpc.trainstation.v1alpha1.GetManyStationsResponseKt.Dsl._create(this.toBuilder()).apply { block() }._build()

public val com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetManyStationsResponseOrBuilder.stationsOrNull: com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.PaginatedStation?
  get() = if (hasStations()) getStations() else null
