// Generated by the protocol buffer compiler. DO NOT EDIT!
// source: trainstation/v1alpha1/station.proto

// Generated files should ignore deprecation warnings
@file:Suppress("DEPRECATION")
package com.example.trainstationapp.data.grpc.trainstation.v1alpha1;

@kotlin.jvm.JvmName("-initializesetFavoriteOneStationResponse")
public inline fun setFavoriteOneStationResponse(block: com.example.trainstationapp.data.grpc.trainstation.v1alpha1.SetFavoriteOneStationResponseKt.Dsl.() -> kotlin.Unit): com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.SetFavoriteOneStationResponse =
  com.example.trainstationapp.data.grpc.trainstation.v1alpha1.SetFavoriteOneStationResponseKt.Dsl._create(com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.SetFavoriteOneStationResponse.newBuilder()).apply { block() }._build()
/**
 * Protobuf type `trainstation.v1alpha1.SetFavoriteOneStationResponse`
 */
public object SetFavoriteOneStationResponseKt {
  @kotlin.OptIn(com.google.protobuf.kotlin.OnlyForUseByGeneratedProtoCode::class)
  @com.google.protobuf.kotlin.ProtoDslMarker
  public class Dsl private constructor(
    private val _builder: com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.SetFavoriteOneStationResponse.Builder
  ) {
    public companion object {
      @kotlin.jvm.JvmSynthetic
      @kotlin.PublishedApi
      internal fun _create(builder: com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.SetFavoriteOneStationResponse.Builder): Dsl = Dsl(builder)
    }

    @kotlin.jvm.JvmSynthetic
    @kotlin.PublishedApi
    internal fun _build(): com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.SetFavoriteOneStationResponse = _builder.build()
  }
}
public inline fun com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.SetFavoriteOneStationResponse.copy(block: com.example.trainstationapp.data.grpc.trainstation.v1alpha1.SetFavoriteOneStationResponseKt.Dsl.() -> kotlin.Unit): com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.SetFavoriteOneStationResponse =
  com.example.trainstationapp.data.grpc.trainstation.v1alpha1.SetFavoriteOneStationResponseKt.Dsl._create(this.toBuilder()).apply { block() }._build()
