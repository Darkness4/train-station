package com.example.trainstationapp.`data`.grpc.auth.v1alpha1

import com.example.trainstationapp.`data`.grpc.auth.v1alpha1.AuthAPIGrpc.getServiceDescriptor
import io.grpc.CallOptions
import io.grpc.CallOptions.DEFAULT
import io.grpc.Channel
import io.grpc.Metadata
import io.grpc.MethodDescriptor
import io.grpc.ServerServiceDefinition
import io.grpc.ServerServiceDefinition.builder
import io.grpc.ServiceDescriptor
import io.grpc.Status.UNIMPLEMENTED
import io.grpc.StatusException
import io.grpc.kotlin.AbstractCoroutineServerImpl
import io.grpc.kotlin.AbstractCoroutineStub
import io.grpc.kotlin.ClientCalls.unaryRpc
import io.grpc.kotlin.ServerCalls.unaryServerMethodDefinition
import io.grpc.kotlin.StubFor
import kotlin.String
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

/**
 * Holder for Kotlin coroutine-based client and server APIs for auth.v1alpha1.AuthAPI.
 */
public object AuthAPIGrpcKt {
  public const val SERVICE_NAME: String = AuthAPIGrpc.SERVICE_NAME

  @JvmStatic
  public val serviceDescriptor: ServiceDescriptor
    get() = AuthAPIGrpc.getServiceDescriptor()

  public val getJWTMethod: MethodDescriptor<AuthProto.GetJWTRequest, AuthProto.GetJWTResponse>
    @JvmStatic
    get() = AuthAPIGrpc.getGetJWTMethod()

  /**
   * A stub for issuing RPCs to a(n) auth.v1alpha1.AuthAPI service as suspending coroutines.
   */
  @StubFor(AuthAPIGrpc::class)
  public class AuthAPICoroutineStub @JvmOverloads constructor(
    channel: Channel,
    callOptions: CallOptions = DEFAULT,
  ) : AbstractCoroutineStub<AuthAPICoroutineStub>(channel, callOptions) {
    public override fun build(channel: Channel, callOptions: CallOptions): AuthAPICoroutineStub =
        AuthAPICoroutineStub(channel, callOptions)

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][io.grpc.Status].  If the RPC completes with another status, a
     * corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun getJWT(request: AuthProto.GetJWTRequest, headers: Metadata = Metadata()):
        AuthProto.GetJWTResponse = unaryRpc(
      channel,
      AuthAPIGrpc.getGetJWTMethod(),
      request,
      callOptions,
      headers
    )
  }

  /**
   * Skeletal implementation of the auth.v1alpha1.AuthAPI service based on Kotlin coroutines.
   */
  public abstract class AuthAPICoroutineImplBase(
    coroutineContext: CoroutineContext = EmptyCoroutineContext,
  ) : AbstractCoroutineServerImpl(coroutineContext) {
    /**
     * Returns the response to an RPC for auth.v1alpha1.AuthAPI.GetJWT.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [io.grpc.Status].  If this method fails with a [java.util.concurrent.CancellationException],
     * the RPC will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun getJWT(request: AuthProto.GetJWTRequest): AuthProto.GetJWTResponse =
        throw
        StatusException(UNIMPLEMENTED.withDescription("Method auth.v1alpha1.AuthAPI.GetJWT is unimplemented"))

    public final override fun bindService(): ServerServiceDefinition =
        builder(getServiceDescriptor())
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = AuthAPIGrpc.getGetJWTMethod(),
      implementation = ::getJWT
    )).build()
  }
}
