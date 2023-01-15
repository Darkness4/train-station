package com.example.trainstationapp.`data`.grpc.grpc.health.v1

import com.example.trainstationapp.`data`.grpc.grpc.health.v1.HealthGrpc.getServiceDescriptor
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
import io.grpc.kotlin.ClientCalls.serverStreamingRpc
import io.grpc.kotlin.ClientCalls.unaryRpc
import io.grpc.kotlin.ServerCalls.serverStreamingServerMethodDefinition
import io.grpc.kotlin.ServerCalls.unaryServerMethodDefinition
import io.grpc.kotlin.StubFor
import kotlin.String
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic
import kotlinx.coroutines.flow.Flow

/**
 * Holder for Kotlin coroutine-based client and server APIs for grpc.health.v1.Health.
 */
public object HealthGrpcKt {
  public const val SERVICE_NAME: String = HealthGrpc.SERVICE_NAME

  @JvmStatic
  public val serviceDescriptor: ServiceDescriptor
    get() = HealthGrpc.getServiceDescriptor()

  public val checkMethod:
      MethodDescriptor<HealthProto.HealthCheckRequest, HealthProto.HealthCheckResponse>
    @JvmStatic
    get() = HealthGrpc.getCheckMethod()

  public val watchMethod:
      MethodDescriptor<HealthProto.HealthCheckRequest, HealthProto.HealthCheckResponse>
    @JvmStatic
    get() = HealthGrpc.getWatchMethod()

  /**
   * A stub for issuing RPCs to a(n) grpc.health.v1.Health service as suspending coroutines.
   */
  @StubFor(HealthGrpc::class)
  public class HealthCoroutineStub @JvmOverloads constructor(
    channel: Channel,
    callOptions: CallOptions = DEFAULT,
  ) : AbstractCoroutineStub<HealthCoroutineStub>(channel, callOptions) {
    public override fun build(channel: Channel, callOptions: CallOptions): HealthCoroutineStub =
        HealthCoroutineStub(channel, callOptions)

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
    public suspend fun check(request: HealthProto.HealthCheckRequest, headers: Metadata =
        Metadata()): HealthProto.HealthCheckResponse = unaryRpc(
      channel,
      HealthGrpc.getCheckMethod(),
      request,
      callOptions,
      headers
    )

    /**
     * Returns a [Flow] that, when collected, executes this RPC and emits responses from the
     * server as they arrive.  That flow finishes normally if the server closes its response with
     * [`Status.OK`][Status], and fails by throwing a [StatusException] otherwise.  If
     * collecting the flow downstream fails exceptionally (including via cancellation), the RPC
     * is cancelled with that exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return A flow that, when collected, emits the responses from the server.
     */
    public fun watch(request: HealthProto.HealthCheckRequest, headers: Metadata = Metadata()):
        Flow<HealthProto.HealthCheckResponse> = serverStreamingRpc(
      channel,
      HealthGrpc.getWatchMethod(),
      request,
      callOptions,
      headers
    )
  }

  /**
   * Skeletal implementation of the grpc.health.v1.Health service based on Kotlin coroutines.
   */
  public abstract class HealthCoroutineImplBase(
    coroutineContext: CoroutineContext = EmptyCoroutineContext,
  ) : AbstractCoroutineServerImpl(coroutineContext) {
    /**
     * Returns the response to an RPC for grpc.health.v1.Health.Check.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun check(request: HealthProto.HealthCheckRequest):
        HealthProto.HealthCheckResponse = throw
        StatusException(UNIMPLEMENTED.withDescription("Method grpc.health.v1.Health.Check is unimplemented"))

    /**
     * Returns a [Flow] of responses to an RPC for grpc.health.v1.Health.Watch.
     *
     * If creating or collecting the returned flow fails with a [StatusException], the RPC
     * will fail with the corresponding [Status].  If it fails with a
     * [java.util.concurrent.CancellationException], the RPC will fail with status
     * `Status.CANCELLED`.  If creating
     * or collecting the returned flow fails for any other reason, the RPC will fail with
     * `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open fun watch(request: HealthProto.HealthCheckRequest):
        Flow<HealthProto.HealthCheckResponse> = throw
        StatusException(UNIMPLEMENTED.withDescription("Method grpc.health.v1.Health.Watch is unimplemented"))

    public final override fun bindService(): ServerServiceDefinition =
        builder(getServiceDescriptor())
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = HealthGrpc.getCheckMethod(),
      implementation = ::check
    ))
      .addMethod(serverStreamingServerMethodDefinition(
      context = this.context,
      descriptor = HealthGrpc.getWatchMethod(),
      implementation = ::watch
    )).build()
  }
}
