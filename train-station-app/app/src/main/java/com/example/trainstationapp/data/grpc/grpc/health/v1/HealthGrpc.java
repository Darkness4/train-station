package com.example.trainstationapp.data.grpc.grpc.health.v1;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.55.1)",
    comments = "Source: grpc/health/v1/health.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class HealthGrpc {

  private HealthGrpc() {}

  public static final String SERVICE_NAME = "grpc.health.v1.Health";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest,
      com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse> getCheckMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Check",
      requestType = com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest.class,
      responseType = com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest,
      com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse> getCheckMethod() {
    io.grpc.MethodDescriptor<com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest, com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse> getCheckMethod;
    if ((getCheckMethod = HealthGrpc.getCheckMethod) == null) {
      synchronized (HealthGrpc.class) {
        if ((getCheckMethod = HealthGrpc.getCheckMethod) == null) {
          HealthGrpc.getCheckMethod = getCheckMethod =
              io.grpc.MethodDescriptor.<com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest, com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Check"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse.getDefaultInstance()))
              .build();
        }
      }
    }
    return getCheckMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest,
      com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse> getWatchMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Watch",
      requestType = com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest.class,
      responseType = com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest,
      com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse> getWatchMethod() {
    io.grpc.MethodDescriptor<com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest, com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse> getWatchMethod;
    if ((getWatchMethod = HealthGrpc.getWatchMethod) == null) {
      synchronized (HealthGrpc.class) {
        if ((getWatchMethod = HealthGrpc.getWatchMethod) == null) {
          HealthGrpc.getWatchMethod = getWatchMethod =
              io.grpc.MethodDescriptor.<com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest, com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Watch"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse.getDefaultInstance()))
              .build();
        }
      }
    }
    return getWatchMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static HealthStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<HealthStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<HealthStub>() {
        @java.lang.Override
        public HealthStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new HealthStub(channel, callOptions);
        }
      };
    return HealthStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static HealthBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<HealthBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<HealthBlockingStub>() {
        @java.lang.Override
        public HealthBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new HealthBlockingStub(channel, callOptions);
        }
      };
    return HealthBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static HealthFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<HealthFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<HealthFutureStub>() {
        @java.lang.Override
        public HealthFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new HealthFutureStub(channel, callOptions);
        }
      };
    return HealthFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void check(com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest request,
        io.grpc.stub.StreamObserver<com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCheckMethod(), responseObserver);
    }

    /**
     */
    default void watch(com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest request,
        io.grpc.stub.StreamObserver<com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getWatchMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service Health.
   */
  public static abstract class HealthImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return HealthGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service Health.
   */
  public static final class HealthStub
      extends io.grpc.stub.AbstractAsyncStub<HealthStub> {
    private HealthStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected HealthStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new HealthStub(channel, callOptions);
    }

    /**
     */
    public void check(com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest request,
        io.grpc.stub.StreamObserver<com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCheckMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void watch(com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest request,
        io.grpc.stub.StreamObserver<com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncServerStreamingCall(
          getChannel().newCall(getWatchMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service Health.
   */
  public static final class HealthBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<HealthBlockingStub> {
    private HealthBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected HealthBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new HealthBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse check(com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCheckMethod(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse> watch(
        com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest request) {
      return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
          getChannel(), getWatchMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service Health.
   */
  public static final class HealthFutureStub
      extends io.grpc.stub.AbstractFutureStub<HealthFutureStub> {
    private HealthFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected HealthFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new HealthFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse> check(
        com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCheckMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CHECK = 0;
  private static final int METHODID_WATCH = 1;

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
        case METHODID_CHECK:
          serviceImpl.check((com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest) request,
              (io.grpc.stub.StreamObserver<com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse>) responseObserver);
          break;
        case METHODID_WATCH:
          serviceImpl.watch((com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest) request,
              (io.grpc.stub.StreamObserver<com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse>) responseObserver);
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
          getCheckMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest,
              com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse>(
                service, METHODID_CHECK)))
        .addMethod(
          getWatchMethod(),
          io.grpc.stub.ServerCalls.asyncServerStreamingCall(
            new MethodHandlers<
              com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest,
              com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse>(
                service, METHODID_WATCH)))
        .build();
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (HealthGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .addMethod(getCheckMethod())
              .addMethod(getWatchMethod())
              .build();
        }
      }
    }
    return result;
  }
}
