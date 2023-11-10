package com.example.trainstationapp.data.grpc.trainstation.v1alpha1;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * StationAPI handles train stations from the SNCF.
 * The API needs the user to be authenticated via the AuthAPI.
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.55.1)",
    comments = "Source: trainstation/v1alpha1/station.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class StationAPIGrpc {

  private StationAPIGrpc() {}

  public static final String SERVICE_NAME = "trainstation.v1alpha1.StationAPI";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetManyStationsRequest,
      com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetManyStationsResponse> getGetManyStationsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetManyStations",
      requestType = com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetManyStationsRequest.class,
      responseType = com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetManyStationsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetManyStationsRequest,
      com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetManyStationsResponse> getGetManyStationsMethod() {
    io.grpc.MethodDescriptor<com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetManyStationsRequest, com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetManyStationsResponse> getGetManyStationsMethod;
    if ((getGetManyStationsMethod = StationAPIGrpc.getGetManyStationsMethod) == null) {
      synchronized (StationAPIGrpc.class) {
        if ((getGetManyStationsMethod = StationAPIGrpc.getGetManyStationsMethod) == null) {
          StationAPIGrpc.getGetManyStationsMethod = getGetManyStationsMethod =
              io.grpc.MethodDescriptor.<com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetManyStationsRequest, com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetManyStationsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetManyStations"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetManyStationsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetManyStationsResponse.getDefaultInstance()))
              .build();
        }
      }
    }
    return getGetManyStationsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetOneStationRequest,
      com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetOneStationResponse> getGetOneStationMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetOneStation",
      requestType = com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetOneStationRequest.class,
      responseType = com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetOneStationResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetOneStationRequest,
      com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetOneStationResponse> getGetOneStationMethod() {
    io.grpc.MethodDescriptor<com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetOneStationRequest, com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetOneStationResponse> getGetOneStationMethod;
    if ((getGetOneStationMethod = StationAPIGrpc.getGetOneStationMethod) == null) {
      synchronized (StationAPIGrpc.class) {
        if ((getGetOneStationMethod = StationAPIGrpc.getGetOneStationMethod) == null) {
          StationAPIGrpc.getGetOneStationMethod = getGetOneStationMethod =
              io.grpc.MethodDescriptor.<com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetOneStationRequest, com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetOneStationResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetOneStation"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetOneStationRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetOneStationResponse.getDefaultInstance()))
              .build();
        }
      }
    }
    return getGetOneStationMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.SetFavoriteOneStationRequest,
      com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.SetFavoriteOneStationResponse> getSetFavoriteOneStationMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SetFavoriteOneStation",
      requestType = com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.SetFavoriteOneStationRequest.class,
      responseType = com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.SetFavoriteOneStationResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.SetFavoriteOneStationRequest,
      com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.SetFavoriteOneStationResponse> getSetFavoriteOneStationMethod() {
    io.grpc.MethodDescriptor<com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.SetFavoriteOneStationRequest, com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.SetFavoriteOneStationResponse> getSetFavoriteOneStationMethod;
    if ((getSetFavoriteOneStationMethod = StationAPIGrpc.getSetFavoriteOneStationMethod) == null) {
      synchronized (StationAPIGrpc.class) {
        if ((getSetFavoriteOneStationMethod = StationAPIGrpc.getSetFavoriteOneStationMethod) == null) {
          StationAPIGrpc.getSetFavoriteOneStationMethod = getSetFavoriteOneStationMethod =
              io.grpc.MethodDescriptor.<com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.SetFavoriteOneStationRequest, com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.SetFavoriteOneStationResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SetFavoriteOneStation"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.SetFavoriteOneStationRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.SetFavoriteOneStationResponse.getDefaultInstance()))
              .build();
        }
      }
    }
    return getSetFavoriteOneStationMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static StationAPIStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<StationAPIStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<StationAPIStub>() {
        @java.lang.Override
        public StationAPIStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new StationAPIStub(channel, callOptions);
        }
      };
    return StationAPIStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static StationAPIBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<StationAPIBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<StationAPIBlockingStub>() {
        @java.lang.Override
        public StationAPIBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new StationAPIBlockingStub(channel, callOptions);
        }
      };
    return StationAPIBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static StationAPIFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<StationAPIFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<StationAPIFutureStub>() {
        @java.lang.Override
        public StationAPIFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new StationAPIFutureStub(channel, callOptions);
        }
      };
    return StationAPIFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * StationAPI handles train stations from the SNCF.
   * The API needs the user to be authenticated via the AuthAPI.
   * </pre>
   */
  public interface AsyncService {

    /**
     * <pre>
     * GetManyStations fetch a paginated list of station.
     * </pre>
     */
    default void getManyStations(com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetManyStationsRequest request,
        io.grpc.stub.StreamObserver<com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetManyStationsResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetManyStationsMethod(), responseObserver);
    }

    /**
     * <pre>
     * GetOneStation fetches the details of a station.
     * </pre>
     */
    default void getOneStation(com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetOneStationRequest request,
        io.grpc.stub.StreamObserver<com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetOneStationResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetOneStationMethod(), responseObserver);
    }

    /**
     * <pre>
     * SetFavoriteOneStation set a station to favorite for a user.
     * </pre>
     */
    default void setFavoriteOneStation(com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.SetFavoriteOneStationRequest request,
        io.grpc.stub.StreamObserver<com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.SetFavoriteOneStationResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSetFavoriteOneStationMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service StationAPI.
   * <pre>
   * StationAPI handles train stations from the SNCF.
   * The API needs the user to be authenticated via the AuthAPI.
   * </pre>
   */
  public static abstract class StationAPIImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return StationAPIGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service StationAPI.
   * <pre>
   * StationAPI handles train stations from the SNCF.
   * The API needs the user to be authenticated via the AuthAPI.
   * </pre>
   */
  public static final class StationAPIStub
      extends io.grpc.stub.AbstractAsyncStub<StationAPIStub> {
    private StationAPIStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StationAPIStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new StationAPIStub(channel, callOptions);
    }

    /**
     * <pre>
     * GetManyStations fetch a paginated list of station.
     * </pre>
     */
    public void getManyStations(com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetManyStationsRequest request,
        io.grpc.stub.StreamObserver<com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetManyStationsResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetManyStationsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * GetOneStation fetches the details of a station.
     * </pre>
     */
    public void getOneStation(com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetOneStationRequest request,
        io.grpc.stub.StreamObserver<com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetOneStationResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetOneStationMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * SetFavoriteOneStation set a station to favorite for a user.
     * </pre>
     */
    public void setFavoriteOneStation(com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.SetFavoriteOneStationRequest request,
        io.grpc.stub.StreamObserver<com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.SetFavoriteOneStationResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSetFavoriteOneStationMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service StationAPI.
   * <pre>
   * StationAPI handles train stations from the SNCF.
   * The API needs the user to be authenticated via the AuthAPI.
   * </pre>
   */
  public static final class StationAPIBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<StationAPIBlockingStub> {
    private StationAPIBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StationAPIBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new StationAPIBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * GetManyStations fetch a paginated list of station.
     * </pre>
     */
    public com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetManyStationsResponse getManyStations(com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetManyStationsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetManyStationsMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * GetOneStation fetches the details of a station.
     * </pre>
     */
    public com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetOneStationResponse getOneStation(com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetOneStationRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetOneStationMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * SetFavoriteOneStation set a station to favorite for a user.
     * </pre>
     */
    public com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.SetFavoriteOneStationResponse setFavoriteOneStation(com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.SetFavoriteOneStationRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSetFavoriteOneStationMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service StationAPI.
   * <pre>
   * StationAPI handles train stations from the SNCF.
   * The API needs the user to be authenticated via the AuthAPI.
   * </pre>
   */
  public static final class StationAPIFutureStub
      extends io.grpc.stub.AbstractFutureStub<StationAPIFutureStub> {
    private StationAPIFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StationAPIFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new StationAPIFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * GetManyStations fetch a paginated list of station.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetManyStationsResponse> getManyStations(
        com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetManyStationsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetManyStationsMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * GetOneStation fetches the details of a station.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetOneStationResponse> getOneStation(
        com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetOneStationRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetOneStationMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * SetFavoriteOneStation set a station to favorite for a user.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.SetFavoriteOneStationResponse> setFavoriteOneStation(
        com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.SetFavoriteOneStationRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSetFavoriteOneStationMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_MANY_STATIONS = 0;
  private static final int METHODID_GET_ONE_STATION = 1;
  private static final int METHODID_SET_FAVORITE_ONE_STATION = 2;

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
        case METHODID_GET_MANY_STATIONS:
          serviceImpl.getManyStations((com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetManyStationsRequest) request,
              (io.grpc.stub.StreamObserver<com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetManyStationsResponse>) responseObserver);
          break;
        case METHODID_GET_ONE_STATION:
          serviceImpl.getOneStation((com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetOneStationRequest) request,
              (io.grpc.stub.StreamObserver<com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetOneStationResponse>) responseObserver);
          break;
        case METHODID_SET_FAVORITE_ONE_STATION:
          serviceImpl.setFavoriteOneStation((com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.SetFavoriteOneStationRequest) request,
              (io.grpc.stub.StreamObserver<com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.SetFavoriteOneStationResponse>) responseObserver);
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
          getGetManyStationsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetManyStationsRequest,
              com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetManyStationsResponse>(
                service, METHODID_GET_MANY_STATIONS)))
        .addMethod(
          getGetOneStationMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetOneStationRequest,
              com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.GetOneStationResponse>(
                service, METHODID_GET_ONE_STATION)))
        .addMethod(
          getSetFavoriteOneStationMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.SetFavoriteOneStationRequest,
              com.example.trainstationapp.data.grpc.trainstation.v1alpha1.StationProto.SetFavoriteOneStationResponse>(
                service, METHODID_SET_FAVORITE_ONE_STATION)))
        .build();
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (StationAPIGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .addMethod(getGetManyStationsMethod())
              .addMethod(getGetOneStationMethod())
              .addMethod(getSetFavoriteOneStationMethod())
              .build();
        }
      }
    }
    return result;
  }
}
