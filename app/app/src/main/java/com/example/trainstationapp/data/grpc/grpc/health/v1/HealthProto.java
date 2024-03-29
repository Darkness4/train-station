// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: grpc/health/v1/health.proto

package com.example.trainstationapp.data.grpc.grpc.health.v1;

public final class HealthProto {
  private HealthProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }
  public interface HealthCheckRequestOrBuilder extends
      // @@protoc_insertion_point(interface_extends:grpc.health.v1.HealthCheckRequest)
      com.google.protobuf.MessageLiteOrBuilder {

    /**
     * <code>string service = 1 [json_name = "service"];</code>
     * @return The service.
     */
    java.lang.String getService();
    /**
     * <code>string service = 1 [json_name = "service"];</code>
     * @return The bytes for service.
     */
    com.google.protobuf.ByteString
        getServiceBytes();
  }
  /**
   * Protobuf type {@code grpc.health.v1.HealthCheckRequest}
   */
  public  static final class HealthCheckRequest extends
      com.google.protobuf.GeneratedMessageLite<
          HealthCheckRequest, HealthCheckRequest.Builder> implements
      // @@protoc_insertion_point(message_implements:grpc.health.v1.HealthCheckRequest)
      HealthCheckRequestOrBuilder {
    private HealthCheckRequest() {
      service_ = "";
    }
    public static final int SERVICE_FIELD_NUMBER = 1;
    private java.lang.String service_;
    /**
     * <code>string service = 1 [json_name = "service"];</code>
     * @return The service.
     */
    @java.lang.Override
    public java.lang.String getService() {
      return service_;
    }
    /**
     * <code>string service = 1 [json_name = "service"];</code>
     * @return The bytes for service.
     */
    @java.lang.Override
    public com.google.protobuf.ByteString
        getServiceBytes() {
      return com.google.protobuf.ByteString.copyFromUtf8(service_);
    }
    /**
     * <code>string service = 1 [json_name = "service"];</code>
     * @param value The service to set.
     */
    private void setService(
        java.lang.String value) {
      java.lang.Class<?> valueClass = value.getClass();
  
      service_ = value;
    }
    /**
     * <code>string service = 1 [json_name = "service"];</code>
     */
    private void clearService() {

      service_ = getDefaultInstance().getService();
    }
    /**
     * <code>string service = 1 [json_name = "service"];</code>
     * @param value The bytes for service to set.
     */
    private void setServiceBytes(
        com.google.protobuf.ByteString value) {
      checkByteStringIsUtf8(value);
      service_ = value.toStringUtf8();

    }

    public static com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, data);
    }
    public static com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, data, extensionRegistry);
    }
    public static com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, data);
    }
    public static com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, data, extensionRegistry);
    }
    public static com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, data);
    }
    public static com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, data, extensionRegistry);
    }
    public static com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, input);
    }
    public static com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return parseDelimitedFrom(DEFAULT_INSTANCE, input);
    }

    public static com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }
    public static com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, input);
    }
    public static com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static Builder newBuilder() {
      return (Builder) DEFAULT_INSTANCE.createBuilder();
    }
    public static Builder newBuilder(com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest prototype) {
      return (Builder) DEFAULT_INSTANCE.createBuilder(prototype);
    }

    /**
     * Protobuf type {@code grpc.health.v1.HealthCheckRequest}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageLite.Builder<
          com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest, Builder> implements
        // @@protoc_insertion_point(builder_implements:grpc.health.v1.HealthCheckRequest)
        com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequestOrBuilder {
      // Construct using com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest.newBuilder()
      private Builder() {
        super(DEFAULT_INSTANCE);
      }


      /**
       * <code>string service = 1 [json_name = "service"];</code>
       * @return The service.
       */
      @java.lang.Override
      public java.lang.String getService() {
        return instance.getService();
      }
      /**
       * <code>string service = 1 [json_name = "service"];</code>
       * @return The bytes for service.
       */
      @java.lang.Override
      public com.google.protobuf.ByteString
          getServiceBytes() {
        return instance.getServiceBytes();
      }
      /**
       * <code>string service = 1 [json_name = "service"];</code>
       * @param value The service to set.
       * @return This builder for chaining.
       */
      public Builder setService(
          java.lang.String value) {
        copyOnWrite();
        instance.setService(value);
        return this;
      }
      /**
       * <code>string service = 1 [json_name = "service"];</code>
       * @return This builder for chaining.
       */
      public Builder clearService() {
        copyOnWrite();
        instance.clearService();
        return this;
      }
      /**
       * <code>string service = 1 [json_name = "service"];</code>
       * @param value The bytes for service to set.
       * @return This builder for chaining.
       */
      public Builder setServiceBytes(
          com.google.protobuf.ByteString value) {
        copyOnWrite();
        instance.setServiceBytes(value);
        return this;
      }

      // @@protoc_insertion_point(builder_scope:grpc.health.v1.HealthCheckRequest)
    }
    @java.lang.Override
    @java.lang.SuppressWarnings({"unchecked", "fallthrough"})
    protected final java.lang.Object dynamicMethod(
        com.google.protobuf.GeneratedMessageLite.MethodToInvoke method,
        java.lang.Object arg0, java.lang.Object arg1) {
      switch (method) {
        case NEW_MUTABLE_INSTANCE: {
          return new com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest();
        }
        case NEW_BUILDER: {
          return new Builder();
        }
        case BUILD_MESSAGE_INFO: {
            java.lang.Object[] objects = new java.lang.Object[] {
              "service_",
            };
            java.lang.String info =
                "\u0000\u0001\u0000\u0000\u0001\u0001\u0001\u0000\u0000\u0000\u0001\u0208";
            return newMessageInfo(DEFAULT_INSTANCE, info, objects);
        }
        // fall through
        case GET_DEFAULT_INSTANCE: {
          return DEFAULT_INSTANCE;
        }
        case GET_PARSER: {
          com.google.protobuf.Parser<com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest> parser = PARSER;
          if (parser == null) {
            synchronized (com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest.class) {
              parser = PARSER;
              if (parser == null) {
                parser =
                    new DefaultInstanceBasedParser<com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest>(
                        DEFAULT_INSTANCE);
                PARSER = parser;
              }
            }
          }
          return parser;
      }
      case GET_MEMOIZED_IS_INITIALIZED: {
        return (byte) 1;
      }
      case SET_MEMOIZED_IS_INITIALIZED: {
        return null;
      }
      }
      throw new UnsupportedOperationException();
    }


    // @@protoc_insertion_point(class_scope:grpc.health.v1.HealthCheckRequest)
    private static final com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest DEFAULT_INSTANCE;
    static {
      HealthCheckRequest defaultInstance = new HealthCheckRequest();
      // New instances are implicitly immutable so no need to make
      // immutable.
      DEFAULT_INSTANCE = defaultInstance;
      com.google.protobuf.GeneratedMessageLite.registerDefaultInstance(
        HealthCheckRequest.class, defaultInstance);
    }

    public static com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckRequest getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static volatile com.google.protobuf.Parser<HealthCheckRequest> PARSER;

    public static com.google.protobuf.Parser<HealthCheckRequest> parser() {
      return DEFAULT_INSTANCE.getParserForType();
    }
  }

  public interface HealthCheckResponseOrBuilder extends
      // @@protoc_insertion_point(interface_extends:grpc.health.v1.HealthCheckResponse)
      com.google.protobuf.MessageLiteOrBuilder {

    /**
     * <code>.grpc.health.v1.HealthCheckResponse.ServingStatus status = 1 [json_name = "status"];</code>
     * @return The enum numeric value on the wire for status.
     */
    int getStatusValue();
    /**
     * <code>.grpc.health.v1.HealthCheckResponse.ServingStatus status = 1 [json_name = "status"];</code>
     * @return The status.
     */
    com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse.ServingStatus getStatus();
  }
  /**
   * Protobuf type {@code grpc.health.v1.HealthCheckResponse}
   */
  public  static final class HealthCheckResponse extends
      com.google.protobuf.GeneratedMessageLite<
          HealthCheckResponse, HealthCheckResponse.Builder> implements
      // @@protoc_insertion_point(message_implements:grpc.health.v1.HealthCheckResponse)
      HealthCheckResponseOrBuilder {
    private HealthCheckResponse() {
    }
    /**
     * Protobuf enum {@code grpc.health.v1.HealthCheckResponse.ServingStatus}
     */
    public enum ServingStatus
        implements com.google.protobuf.Internal.EnumLite {
      /**
       * <code>UNKNOWN = 0;</code>
       */
      UNKNOWN(0),
      /**
       * <code>SERVING = 1;</code>
       */
      SERVING(1),
      /**
       * <code>NOT_SERVING = 2;</code>
       */
      NOT_SERVING(2),
      /**
       * <pre>
       * Used only by the Watch method.
       * </pre>
       *
       * <code>SERVICE_UNKNOWN = 3;</code>
       */
      SERVICE_UNKNOWN(3),
      UNRECOGNIZED(-1),
      ;

      /**
       * <code>UNKNOWN = 0;</code>
       */
      public static final int UNKNOWN_VALUE = 0;
      /**
       * <code>SERVING = 1;</code>
       */
      public static final int SERVING_VALUE = 1;
      /**
       * <code>NOT_SERVING = 2;</code>
       */
      public static final int NOT_SERVING_VALUE = 2;
      /**
       * <pre>
       * Used only by the Watch method.
       * </pre>
       *
       * <code>SERVICE_UNKNOWN = 3;</code>
       */
      public static final int SERVICE_UNKNOWN_VALUE = 3;


      @java.lang.Override
      public final int getNumber() {
        if (this == UNRECOGNIZED) {
          throw new java.lang.IllegalArgumentException(
              "Can't get the number of an unknown enum value.");
        }
        return value;
      }

      /**
       * @param value The number of the enum to look for.
       * @return The enum associated with the given number.
       * @deprecated Use {@link #forNumber(int)} instead.
       */
      @java.lang.Deprecated
      public static ServingStatus valueOf(int value) {
        return forNumber(value);
      }

      public static ServingStatus forNumber(int value) {
        switch (value) {
          case 0: return UNKNOWN;
          case 1: return SERVING;
          case 2: return NOT_SERVING;
          case 3: return SERVICE_UNKNOWN;
          default: return null;
        }
      }

      public static com.google.protobuf.Internal.EnumLiteMap<ServingStatus>
          internalGetValueMap() {
        return internalValueMap;
      }
      private static final com.google.protobuf.Internal.EnumLiteMap<
          ServingStatus> internalValueMap =
            new com.google.protobuf.Internal.EnumLiteMap<ServingStatus>() {
              @java.lang.Override
              public ServingStatus findValueByNumber(int number) {
                return ServingStatus.forNumber(number);
              }
            };

      public static com.google.protobuf.Internal.EnumVerifier 
          internalGetVerifier() {
        return ServingStatusVerifier.INSTANCE;
      }

      private static final class ServingStatusVerifier implements 
           com.google.protobuf.Internal.EnumVerifier { 
              static final com.google.protobuf.Internal.EnumVerifier           INSTANCE = new ServingStatusVerifier();
              @java.lang.Override
              public boolean isInRange(int number) {
                return ServingStatus.forNumber(number) != null;
              }
            };

      private final int value;

      private ServingStatus(int value) {
        this.value = value;
      }

      // @@protoc_insertion_point(enum_scope:grpc.health.v1.HealthCheckResponse.ServingStatus)
    }

    public static final int STATUS_FIELD_NUMBER = 1;
    private int status_;
    /**
     * <code>.grpc.health.v1.HealthCheckResponse.ServingStatus status = 1 [json_name = "status"];</code>
     * @return The enum numeric value on the wire for status.
     */
    @java.lang.Override
    public int getStatusValue() {
      return status_;
    }
    /**
     * <code>.grpc.health.v1.HealthCheckResponse.ServingStatus status = 1 [json_name = "status"];</code>
     * @return The status.
     */
    @java.lang.Override
    public com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse.ServingStatus getStatus() {
      com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse.ServingStatus result = com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse.ServingStatus.forNumber(status_);
      return result == null ? com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse.ServingStatus.UNRECOGNIZED : result;
    }
    /**
     * <code>.grpc.health.v1.HealthCheckResponse.ServingStatus status = 1 [json_name = "status"];</code>
     * @param value The enum numeric value on the wire for status to set.
     */
    private void setStatusValue(int value) {
        status_ = value;
    }
    /**
     * <code>.grpc.health.v1.HealthCheckResponse.ServingStatus status = 1 [json_name = "status"];</code>
     * @param value The status to set.
     */
    private void setStatus(com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse.ServingStatus value) {
      status_ = value.getNumber();

    }
    /**
     * <code>.grpc.health.v1.HealthCheckResponse.ServingStatus status = 1 [json_name = "status"];</code>
     */
    private void clearStatus() {

      status_ = 0;
    }

    public static com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, data);
    }
    public static com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, data, extensionRegistry);
    }
    public static com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, data);
    }
    public static com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, data, extensionRegistry);
    }
    public static com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, data);
    }
    public static com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, data, extensionRegistry);
    }
    public static com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, input);
    }
    public static com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return parseDelimitedFrom(DEFAULT_INSTANCE, input);
    }

    public static com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }
    public static com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, input);
    }
    public static com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static Builder newBuilder() {
      return (Builder) DEFAULT_INSTANCE.createBuilder();
    }
    public static Builder newBuilder(com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse prototype) {
      return (Builder) DEFAULT_INSTANCE.createBuilder(prototype);
    }

    /**
     * Protobuf type {@code grpc.health.v1.HealthCheckResponse}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageLite.Builder<
          com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse, Builder> implements
        // @@protoc_insertion_point(builder_implements:grpc.health.v1.HealthCheckResponse)
        com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponseOrBuilder {
      // Construct using com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse.newBuilder()
      private Builder() {
        super(DEFAULT_INSTANCE);
      }


      /**
       * <code>.grpc.health.v1.HealthCheckResponse.ServingStatus status = 1 [json_name = "status"];</code>
       * @return The enum numeric value on the wire for status.
       */
      @java.lang.Override
      public int getStatusValue() {
        return instance.getStatusValue();
      }
      /**
       * <code>.grpc.health.v1.HealthCheckResponse.ServingStatus status = 1 [json_name = "status"];</code>
       * @param value The status to set.
       * @return This builder for chaining.
       */
      public Builder setStatusValue(int value) {
        copyOnWrite();
        instance.setStatusValue(value);
        return this;
      }
      /**
       * <code>.grpc.health.v1.HealthCheckResponse.ServingStatus status = 1 [json_name = "status"];</code>
       * @return The status.
       */
      @java.lang.Override
      public com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse.ServingStatus getStatus() {
        return instance.getStatus();
      }
      /**
       * <code>.grpc.health.v1.HealthCheckResponse.ServingStatus status = 1 [json_name = "status"];</code>
       * @param value The enum numeric value on the wire for status to set.
       * @return This builder for chaining.
       */
      public Builder setStatus(com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse.ServingStatus value) {
        copyOnWrite();
        instance.setStatus(value);
        return this;
      }
      /**
       * <code>.grpc.health.v1.HealthCheckResponse.ServingStatus status = 1 [json_name = "status"];</code>
       * @return This builder for chaining.
       */
      public Builder clearStatus() {
        copyOnWrite();
        instance.clearStatus();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:grpc.health.v1.HealthCheckResponse)
    }
    @java.lang.Override
    @java.lang.SuppressWarnings({"unchecked", "fallthrough"})
    protected final java.lang.Object dynamicMethod(
        com.google.protobuf.GeneratedMessageLite.MethodToInvoke method,
        java.lang.Object arg0, java.lang.Object arg1) {
      switch (method) {
        case NEW_MUTABLE_INSTANCE: {
          return new com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse();
        }
        case NEW_BUILDER: {
          return new Builder();
        }
        case BUILD_MESSAGE_INFO: {
            java.lang.Object[] objects = new java.lang.Object[] {
              "status_",
            };
            java.lang.String info =
                "\u0000\u0001\u0000\u0000\u0001\u0001\u0001\u0000\u0000\u0000\u0001\f";
            return newMessageInfo(DEFAULT_INSTANCE, info, objects);
        }
        // fall through
        case GET_DEFAULT_INSTANCE: {
          return DEFAULT_INSTANCE;
        }
        case GET_PARSER: {
          com.google.protobuf.Parser<com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse> parser = PARSER;
          if (parser == null) {
            synchronized (com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse.class) {
              parser = PARSER;
              if (parser == null) {
                parser =
                    new DefaultInstanceBasedParser<com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse>(
                        DEFAULT_INSTANCE);
                PARSER = parser;
              }
            }
          }
          return parser;
      }
      case GET_MEMOIZED_IS_INITIALIZED: {
        return (byte) 1;
      }
      case SET_MEMOIZED_IS_INITIALIZED: {
        return null;
      }
      }
      throw new UnsupportedOperationException();
    }


    // @@protoc_insertion_point(class_scope:grpc.health.v1.HealthCheckResponse)
    private static final com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse DEFAULT_INSTANCE;
    static {
      HealthCheckResponse defaultInstance = new HealthCheckResponse();
      // New instances are implicitly immutable so no need to make
      // immutable.
      DEFAULT_INSTANCE = defaultInstance;
      com.google.protobuf.GeneratedMessageLite.registerDefaultInstance(
        HealthCheckResponse.class, defaultInstance);
    }

    public static com.example.trainstationapp.data.grpc.grpc.health.v1.HealthProto.HealthCheckResponse getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static volatile com.google.protobuf.Parser<HealthCheckResponse> PARSER;

    public static com.google.protobuf.Parser<HealthCheckResponse> parser() {
      return DEFAULT_INSTANCE.getParserForType();
    }
  }


  static {
  }

  // @@protoc_insertion_point(outer_class_scope)
}
