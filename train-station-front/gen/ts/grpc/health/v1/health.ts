// @generated by protobuf-ts 2.8.2
// @generated from protobuf file "grpc/health/v1/health.proto" (package "grpc.health.v1", syntax proto3)
// tslint:disable
import { ServiceType } from "@protobuf-ts/runtime-rpc";
import { MessageType } from "@protobuf-ts/runtime";
/**
 * @generated from protobuf message grpc.health.v1.HealthCheckRequest
 */
export interface HealthCheckRequest {
    /**
     * @generated from protobuf field: string service = 1;
     */
    service: string;
}
/**
 * @generated from protobuf message grpc.health.v1.HealthCheckResponse
 */
export interface HealthCheckResponse {
    /**
     * @generated from protobuf field: grpc.health.v1.HealthCheckResponse.ServingStatus status = 1;
     */
    status: HealthCheckResponse_ServingStatus;
}
/**
 * @generated from protobuf enum grpc.health.v1.HealthCheckResponse.ServingStatus
 */
export enum HealthCheckResponse_ServingStatus {
    /**
     * @generated from protobuf enum value: UNKNOWN = 0;
     */
    UNKNOWN = 0,
    /**
     * @generated from protobuf enum value: SERVING = 1;
     */
    SERVING = 1,
    /**
     * @generated from protobuf enum value: NOT_SERVING = 2;
     */
    NOT_SERVING = 2,
    /**
     * Used only by the Watch method.
     *
     * @generated from protobuf enum value: SERVICE_UNKNOWN = 3;
     */
    SERVICE_UNKNOWN = 3
}
// @generated message type with reflection information, may provide speed optimized methods
class HealthCheckRequest$Type extends MessageType<HealthCheckRequest> {
    constructor() {
        super("grpc.health.v1.HealthCheckRequest", [
            { no: 1, name: "service", kind: "scalar", T: 9 /*ScalarType.STRING*/ }
        ]);
    }
}
/**
 * @generated MessageType for protobuf message grpc.health.v1.HealthCheckRequest
 */
export const HealthCheckRequest = new HealthCheckRequest$Type();
// @generated message type with reflection information, may provide speed optimized methods
class HealthCheckResponse$Type extends MessageType<HealthCheckResponse> {
    constructor() {
        super("grpc.health.v1.HealthCheckResponse", [
            { no: 1, name: "status", kind: "enum", T: () => ["grpc.health.v1.HealthCheckResponse.ServingStatus", HealthCheckResponse_ServingStatus] }
        ]);
    }
}
/**
 * @generated MessageType for protobuf message grpc.health.v1.HealthCheckResponse
 */
export const HealthCheckResponse = new HealthCheckResponse$Type();
/**
 * @generated ServiceType for protobuf service grpc.health.v1.Health
 */
export const Health = new ServiceType("grpc.health.v1.Health", [
    { name: "Check", options: {}, I: HealthCheckRequest, O: HealthCheckResponse },
    { name: "Watch", serverStreaming: true, options: {}, I: HealthCheckRequest, O: HealthCheckResponse }
]);
