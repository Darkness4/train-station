//Generated by the protocol buffer compiler. DO NOT EDIT!
// source: trainstation/v1alpha1/station.proto

package com.example.trainstationapp.data.grpc.trainstation.v1alpha1;

@kotlin.jvm.JvmName("-initializegetOneStationRequest")
inline fun getOneStationRequest(block: com.example.trainstationapp.data.grpc.trainstation.v1alpha1.GetOneStationRequestKt.Dsl.() -> kotlin.Unit): com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetOneStationRequest =
  com.example.trainstationapp.data.grpc.trainstation.v1alpha1.GetOneStationRequestKt.Dsl._create(com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetOneStationRequest.newBuilder()).apply { block() }._build()
object GetOneStationRequestKt {
  @kotlin.OptIn(com.google.protobuf.kotlin.OnlyForUseByGeneratedProtoCode::class)
  @com.google.protobuf.kotlin.ProtoDslMarker
  class Dsl private constructor(
    private val _builder: com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetOneStationRequest.Builder
  ) {
    companion object {
      @kotlin.jvm.JvmSynthetic
      @kotlin.PublishedApi
      internal fun _create(builder: com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetOneStationRequest.Builder): Dsl = Dsl(builder)
    }

    @kotlin.jvm.JvmSynthetic
    @kotlin.PublishedApi
    internal fun _build(): com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetOneStationRequest = _builder.build()

    /**
     * <code>string id = 1 [json_name = "id"];</code>
     */
    var id: kotlin.String
      @JvmName("getId")
      get() = _builder.getId()
      @JvmName("setId")
      set(value) {
        _builder.setId(value)
      }
    /**
     * <code>string id = 1 [json_name = "id"];</code>
     */
    fun clearId() {
      _builder.clearId()
    }

    /**
     * <code>string token = 2 [json_name = "token"];</code>
     */
    var token: kotlin.String
      @JvmName("getToken")
      get() = _builder.getToken()
      @JvmName("setToken")
      set(value) {
        _builder.setToken(value)
      }
    /**
     * <code>string token = 2 [json_name = "token"];</code>
     */
    fun clearToken() {
      _builder.clearToken()
    }
  }
}
inline fun com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetOneStationRequest.copy(block: com.example.trainstationapp.data.grpc.trainstation.v1alpha1.GetOneStationRequestKt.Dsl.() -> kotlin.Unit): com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetOneStationRequest =
  com.example.trainstationapp.data.grpc.trainstation.v1alpha1.GetOneStationRequestKt.Dsl._create(this.toBuilder()).apply { block() }._build()

