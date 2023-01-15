package com.example.trainstationapp.`data`.grpc.trainstation.v1alpha1

import com.example.trainstationapp.`data`.grpc.trainstation.v1alpha1.StationAPIGrpc.getServiceDescriptor
import io.grpc.CallOptions
import io.grpc.CallOptions.DEFAULT
import io.grpc.Channel
import io.grpc.Metadata
import io.grpc.MethodDescriptor
import io.grpc.ServerServiceDefinition
import io.grpc.ServerServiceDefinition.builder
import io.grpc.ServiceDescriptor
import io.grpc.Status
import io.grpc.Status.UNIMPLEMENTED
import io.grpc.StatusException
import io.grpc.kotlin.AbstractCoroutineServerImpl
import io.grpc.kotlin.AbstractCoroutineStub
import io.grpc.kotlin.ClientCalls
import io.grpc.kotlin.ClientCalls.unaryRpc
import io.grpc.kotlin.ServerCalls
import io.grpc.kotlin.ServerCalls.unaryServerMethodDefinition
import io.grpc.kotlin.StubFor
import kotlin.String
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

/**
 * Holder for Kotlin coroutine-based client and server APIs for trainstation.v1alpha1.StationAPI.
 */
public object StationAPIGrpcKt {
  public const val SERVICE_NAME: String = StationAPIGrpc.SERVICE_NAME

  @JvmStatic
  public val serviceDescriptor: ServiceDescriptor
    get() = StationAPIGrpc.getServiceDescriptor()

  public val getManyStationsMethod:
      MethodDescriptor<StationProto.GetManyStationsRequest, StationProto.GetManyStationsResponse>
    @JvmStatic
    get() = StationAPIGrpc.getGetManyStationsMethod()

  public val getOneStationMethod:
      MethodDescriptor<StationProto.GetOneStationRequest, StationProto.GetOneStationResponse>
    @JvmStatic
    get() = StationAPIGrpc.getGetOneStationMethod()

  public val setFavoriteOneStationMethod:
      MethodDescriptor<StationProto.SetFavoriteOneStationRequest, StationProto.SetFavoriteOneStationResponse>
    @JvmStatic
    get() = StationAPIGrpc.getSetFavoriteOneStationMethod()

  /**
   * A stub for issuing RPCs to a(n) trainstation.v1alpha1.StationAPI service as suspending
   * coroutines.
   */
  @StubFor(StationAPIGrpc::class)
  public class StationAPICoroutineStub @JvmOverloads constructor(
    channel: Channel,
    callOptions: CallOptions = DEFAULT,
  ) : AbstractCoroutineStub<StationAPICoroutineStub>(channel, callOptions) {
    public override fun build(channel: Channel, callOptions: CallOptions): StationAPICoroutineStub =
        StationAPICoroutineStub(channel, callOptions)

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun getManyStations(request: StationProto.GetManyStationsRequest,
        headers: Metadata = Metadata()): StationProto.GetManyStationsResponse = unaryRpc(
      channel,
      StationAPIGrpc.getGetManyStationsMethod(),
      request,
      callOptions,
      headers
    )

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun getOneStation(request: StationProto.GetOneStationRequest, headers: Metadata =
        Metadata()): StationProto.GetOneStationResponse = unaryRpc(
      channel,
      StationAPIGrpc.getGetOneStationMethod(),
      request,
      callOptions,
      headers
    )

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun setFavoriteOneStation(request: StationProto.SetFavoriteOneStationRequest,
        headers: Metadata = Metadata()): StationProto.SetFavoriteOneStationResponse = unaryRpc(
      channel,
      StationAPIGrpc.getSetFavoriteOneStationMethod(),
      request,
      callOptions,
      headers
    )
  }

  /**
   * Skeletal implementation of the trainstation.v1alpha1.StationAPI service based on Kotlin
   * coroutines.
   */
  public abstract class StationAPICoroutineImplBase(
    coroutineContext: CoroutineContext = EmptyCoroutineContext,
  ) : AbstractCoroutineServerImpl(coroutineContext) {
    /**
     * Returns the response to an RPC for trainstation.v1alpha1.StationAPI.GetManyStations.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun getManyStations(request: StationProto.GetManyStationsRequest):
        StationProto.GetManyStationsResponse = throw
        StatusException(UNIMPLEMENTED.withDescription("Method trainstation.v1alpha1.StationAPI.GetManyStations is unimplemented"))

    /**
     * Returns the response to an RPC for trainstation.v1alpha1.StationAPI.GetOneStation.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun getOneStation(request: StationProto.GetOneStationRequest):
        StationProto.GetOneStationResponse = throw
        StatusException(UNIMPLEMENTED.withDescription("Method trainstation.v1alpha1.StationAPI.GetOneStation is unimplemented"))

    /**
     * Returns the response to an RPC for trainstation.v1alpha1.StationAPI.SetFavoriteOneStation.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend
        fun setFavoriteOneStation(request: StationProto.SetFavoriteOneStationRequest):
        StationProto.SetFavoriteOneStationResponse = throw
        StatusException(UNIMPLEMENTED.withDescription("Method trainstation.v1alpha1.StationAPI.SetFavoriteOneStation is unimplemented"))

    public final override fun bindService(): ServerServiceDefinition =
        builder(getServiceDescriptor())
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = StationAPIGrpc.getGetManyStationsMethod(),
      implementation = ::getManyStations
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = StationAPIGrpc.getGetOneStationMethod(),
      implementation = ::getOneStation
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = StationAPIGrpc.getSetFavoriteOneStationMethod(),
      implementation = ::setFavoriteOneStation
    )).build()
  }
}
