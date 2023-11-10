package com.example.trainstationapp.data.grpc.auth.v1alpha1;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * AuthAPI is the main authentication API between the backend and the frontends.
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.55.1)",
    comments = "Source: auth/v1alpha1/auth.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class AuthAPIGrpc {

  private AuthAPIGrpc() {}

  public static final String SERVICE_NAME = "auth.v1alpha1.AuthAPI";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.example.trainstationapp.data.grpc.auth.v1alpha1.AuthProto.GetJWTRequest,
      com.example.trainstationapp.data.grpc.auth.v1alpha1.AuthProto.GetJWTResponse> getGetJWTMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetJWT",
      requestType = com.example.trainstationapp.data.grpc.auth.v1alpha1.AuthProto.GetJWTRequest.class,
      responseType = com.example.trainstationapp.data.grpc.auth.v1alpha1.AuthProto.GetJWTResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.trainstationapp.data.grpc.auth.v1alpha1.AuthProto.GetJWTRequest,
      com.example.trainstationapp.data.grpc.auth.v1alpha1.AuthProto.GetJWTResponse> getGetJWTMethod() {
    io.grpc.MethodDescriptor<com.example.trainstationapp.data.grpc.auth.v1alpha1.AuthProto.GetJWTRequest, com.example.trainstationapp.data.grpc.auth.v1alpha1.AuthProto.GetJWTResponse> getGetJWTMethod;
    if ((getGetJWTMethod = AuthAPIGrpc.getGetJWTMethod) == null) {
      synchronized (AuthAPIGrpc.class) {
        if ((getGetJWTMethod = AuthAPIGrpc.getGetJWTMethod) == null) {
          AuthAPIGrpc.getGetJWTMethod = getGetJWTMethod =
              io.grpc.MethodDescriptor.<com.example.trainstationapp.data.grpc.auth.v1alpha1.AuthProto.GetJWTRequest, com.example.trainstationapp.data.grpc.auth.v1alpha1.AuthProto.GetJWTResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetJWT"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  com.example.trainstationapp.data.grpc.auth.v1alpha1.AuthProto.GetJWTRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  com.example.trainstationapp.data.grpc.auth.v1alpha1.AuthProto.GetJWTResponse.getDefaultInstance()))
              .build();
        }
      }
    }
    return getGetJWTMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static AuthAPIStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AuthAPIStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AuthAPIStub>() {
        @java.lang.Override
        public AuthAPIStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AuthAPIStub(channel, callOptions);
        }
      };
    return AuthAPIStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static AuthAPIBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AuthAPIBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AuthAPIBlockingStub>() {
        @java.lang.Override
        public AuthAPIBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AuthAPIBlockingStub(channel, callOptions);
        }
      };
    return AuthAPIBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static AuthAPIFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AuthAPIFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AuthAPIFutureStub>() {
        @java.lang.Override
        public AuthAPIFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AuthAPIFutureStub(channel, callOptions);
        }
      };
    return AuthAPIFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * AuthAPI is the main authentication API between the backend and the frontends.
   * </pre>
   */
  public interface AsyncService {

    /**
     */
    default void getJWT(com.example.trainstationapp.data.grpc.auth.v1alpha1.AuthProto.GetJWTRequest request,
        io.grpc.stub.StreamObserver<com.example.trainstationapp.data.grpc.auth.v1alpha1.AuthProto.GetJWTResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetJWTMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service AuthAPI.
   * <pre>
   * AuthAPI is the main authentication API between the backend and the frontends.
   * </pre>
   */
  public static abstract class AuthAPIImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return AuthAPIGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service AuthAPI.
   * <pre>
   * AuthAPI is the main authentication API between the backend and the frontends.
   * </pre>
   */
  public static final class AuthAPIStub
      extends io.grpc.stub.AbstractAsyncStub<AuthAPIStub> {
    private AuthAPIStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AuthAPIStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AuthAPIStub(channel, callOptions);
    }

    /**
     */
    public void getJWT(com.example.trainstationapp.data.grpc.auth.v1alpha1.AuthProto.GetJWTRequest request,
        io.grpc.stub.StreamObserver<com.example.trainstationapp.data.grpc.auth.v1alpha1.AuthProto.GetJWTResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetJWTMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service AuthAPI.
   * <pre>
   * AuthAPI is the main authentication API between the backend and the frontends.
   * </pre>
   */
  public static final class AuthAPIBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<AuthAPIBlockingStub> {
    private AuthAPIBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AuthAPIBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AuthAPIBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.example.trainstationapp.data.grpc.auth.v1alpha1.AuthProto.GetJWTResponse getJWT(com.example.trainstationapp.data.grpc.auth.v1alpha1.AuthProto.GetJWTRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetJWTMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service AuthAPI.
   * <pre>
   * AuthAPI is the main authentication API between the backend and the frontends.
   * </pre>
   */
  public static final class AuthAPIFutureStub
      extends io.grpc.stub.AbstractFutureStub<AuthAPIFutureStub> {
    private AuthAPIFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AuthAPIFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AuthAPIFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.trainstationapp.data.grpc.auth.v1alpha1.AuthProto.GetJWTResponse> getJWT(
        com.example.trainstationapp.data.grpc.auth.v1alpha1.AuthProto.GetJWTRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetJWTMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_JWT = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_JWT:
          serviceImpl.getJWT((com.example.trainstationapp.data.grpc.auth.v1alpha1.AuthProto.GetJWTRequest) request,
              (io.grpc.stub.StreamObserver<com.example.trainstationapp.data.grpc.auth.v1alpha1.AuthProto.GetJWTResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getGetJWTMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.trainstationapp.data.grpc.auth.v1alpha1.AuthProto.GetJWTRequest,
              com.example.trainstationapp.data.grpc.auth.v1alpha1.AuthProto.GetJWTResponse>(
                service, METHODID_GET_JWT)))
        .build();
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (AuthAPIGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .addMethod(getGetJWTMethod())
              .build();
        }
      }
    }
    return result;
  }
}
