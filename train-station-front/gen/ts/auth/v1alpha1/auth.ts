// @generated by protobuf-ts 2.8.2
// @generated from protobuf file "auth/v1alpha1/auth.proto" (package "auth.v1alpha1", syntax proto3)
// tslint:disable
import { ServiceType } from "@protobuf-ts/runtime-rpc";
import type { BinaryWriteOptions } from "@protobuf-ts/runtime";
import type { IBinaryWriter } from "@protobuf-ts/runtime";
import { WireType } from "@protobuf-ts/runtime";
import type { BinaryReadOptions } from "@protobuf-ts/runtime";
import type { IBinaryReader } from "@protobuf-ts/runtime";
import { UnknownFieldHandler } from "@protobuf-ts/runtime";
import type { PartialMessage } from "@protobuf-ts/runtime";
import { reflectionMergePartial } from "@protobuf-ts/runtime";
import { MESSAGE_TYPE } from "@protobuf-ts/runtime";
import { MessageType } from "@protobuf-ts/runtime";
/**
 * Account are the credentials for authentication API.
 *
 * @generated from protobuf message auth.v1alpha1.Account
 */
export interface Account {
    /**
     *
     * ID of the provider used for this account.
     *
     * Based on the ID of the provider, it will check the access token on the OAuth/OIDC Provider.
     *
     * @generated from protobuf field: string provider = 1;
     */
    provider: string;
    /**
     * Provider's type for this account, oauth or oidc.
     *
     * @generated from protobuf field: string type = 2;
     */
    type: string;
    /**
     * The provider account ID.
     *
     * @generated from protobuf field: string provider_account_id = 3;
     */
    providerAccountId: string;
    /**
     * The provider access_token.
     *
     * @generated from protobuf field: string access_token = 4;
     */
    accessToken: string;
}
/**
 * @generated from protobuf message auth.v1alpha1.GetJWTRequest
 */
export interface GetJWTRequest {
    /**
     * @generated from protobuf field: auth.v1alpha1.Account account = 1;
     */
    account?: Account;
}
/**
 * @generated from protobuf message auth.v1alpha1.GetJWTResponse
 */
export interface GetJWTResponse {
    /**
     * @generated from protobuf field: string token = 1;
     */
    token: string;
}
// @generated message type with reflection information, may provide speed optimized methods
class Account$Type extends MessageType<Account> {
    constructor() {
        super("auth.v1alpha1.Account", [
            { no: 1, name: "provider", kind: "scalar", T: 9 /*ScalarType.STRING*/ },
            { no: 2, name: "type", kind: "scalar", T: 9 /*ScalarType.STRING*/ },
            { no: 3, name: "provider_account_id", kind: "scalar", T: 9 /*ScalarType.STRING*/ },
            { no: 4, name: "access_token", kind: "scalar", T: 9 /*ScalarType.STRING*/ }
        ]);
    }
    create(value?: PartialMessage<Account>): Account {
        const message = { provider: "", type: "", providerAccountId: "", accessToken: "" };
        globalThis.Object.defineProperty(message, MESSAGE_TYPE, { enumerable: false, value: this });
        if (value !== undefined)
            reflectionMergePartial<Account>(this, message, value);
        return message;
    }
    internalBinaryRead(reader: IBinaryReader, length: number, options: BinaryReadOptions, target?: Account): Account {
        let message = target ?? this.create(), end = reader.pos + length;
        while (reader.pos < end) {
            let [fieldNo, wireType] = reader.tag();
            switch (fieldNo) {
                case /* string provider */ 1:
                    message.provider = reader.string();
                    break;
                case /* string type */ 2:
                    message.type = reader.string();
                    break;
                case /* string provider_account_id */ 3:
                    message.providerAccountId = reader.string();
                    break;
                case /* string access_token */ 4:
                    message.accessToken = reader.string();
                    break;
                default:
                    let u = options.readUnknownField;
                    if (u === "throw")
                        throw new globalThis.Error(`Unknown field ${fieldNo} (wire type ${wireType}) for ${this.typeName}`);
                    let d = reader.skip(wireType);
                    if (u !== false)
                        (u === true ? UnknownFieldHandler.onRead : u)(this.typeName, message, fieldNo, wireType, d);
            }
        }
        return message;
    }
    internalBinaryWrite(message: Account, writer: IBinaryWriter, options: BinaryWriteOptions): IBinaryWriter {
        /* string provider = 1; */
        if (message.provider !== "")
            writer.tag(1, WireType.LengthDelimited).string(message.provider);
        /* string type = 2; */
        if (message.type !== "")
            writer.tag(2, WireType.LengthDelimited).string(message.type);
        /* string provider_account_id = 3; */
        if (message.providerAccountId !== "")
            writer.tag(3, WireType.LengthDelimited).string(message.providerAccountId);
        /* string access_token = 4; */
        if (message.accessToken !== "")
            writer.tag(4, WireType.LengthDelimited).string(message.accessToken);
        let u = options.writeUnknownFields;
        if (u !== false)
            (u == true ? UnknownFieldHandler.onWrite : u)(this.typeName, message, writer);
        return writer;
    }
}
/**
 * @generated MessageType for protobuf message auth.v1alpha1.Account
 */
export const Account = new Account$Type();
// @generated message type with reflection information, may provide speed optimized methods
class GetJWTRequest$Type extends MessageType<GetJWTRequest> {
    constructor() {
        super("auth.v1alpha1.GetJWTRequest", [
            { no: 1, name: "account", kind: "message", T: () => Account }
        ]);
    }
    create(value?: PartialMessage<GetJWTRequest>): GetJWTRequest {
        const message = {};
        globalThis.Object.defineProperty(message, MESSAGE_TYPE, { enumerable: false, value: this });
        if (value !== undefined)
            reflectionMergePartial<GetJWTRequest>(this, message, value);
        return message;
    }
    internalBinaryRead(reader: IBinaryReader, length: number, options: BinaryReadOptions, target?: GetJWTRequest): GetJWTRequest {
        let message = target ?? this.create(), end = reader.pos + length;
        while (reader.pos < end) {
            let [fieldNo, wireType] = reader.tag();
            switch (fieldNo) {
                case /* auth.v1alpha1.Account account */ 1:
                    message.account = Account.internalBinaryRead(reader, reader.uint32(), options, message.account);
                    break;
                default:
                    let u = options.readUnknownField;
                    if (u === "throw")
                        throw new globalThis.Error(`Unknown field ${fieldNo} (wire type ${wireType}) for ${this.typeName}`);
                    let d = reader.skip(wireType);
                    if (u !== false)
                        (u === true ? UnknownFieldHandler.onRead : u)(this.typeName, message, fieldNo, wireType, d);
            }
        }
        return message;
    }
    internalBinaryWrite(message: GetJWTRequest, writer: IBinaryWriter, options: BinaryWriteOptions): IBinaryWriter {
        /* auth.v1alpha1.Account account = 1; */
        if (message.account)
            Account.internalBinaryWrite(message.account, writer.tag(1, WireType.LengthDelimited).fork(), options).join();
        let u = options.writeUnknownFields;
        if (u !== false)
            (u == true ? UnknownFieldHandler.onWrite : u)(this.typeName, message, writer);
        return writer;
    }
}
/**
 * @generated MessageType for protobuf message auth.v1alpha1.GetJWTRequest
 */
export const GetJWTRequest = new GetJWTRequest$Type();
// @generated message type with reflection information, may provide speed optimized methods
class GetJWTResponse$Type extends MessageType<GetJWTResponse> {
    constructor() {
        super("auth.v1alpha1.GetJWTResponse", [
            { no: 1, name: "token", kind: "scalar", T: 9 /*ScalarType.STRING*/ }
        ]);
    }
    create(value?: PartialMessage<GetJWTResponse>): GetJWTResponse {
        const message = { token: "" };
        globalThis.Object.defineProperty(message, MESSAGE_TYPE, { enumerable: false, value: this });
        if (value !== undefined)
            reflectionMergePartial<GetJWTResponse>(this, message, value);
        return message;
    }
    internalBinaryRead(reader: IBinaryReader, length: number, options: BinaryReadOptions, target?: GetJWTResponse): GetJWTResponse {
        let message = target ?? this.create(), end = reader.pos + length;
        while (reader.pos < end) {
            let [fieldNo, wireType] = reader.tag();
            switch (fieldNo) {
                case /* string token */ 1:
                    message.token = reader.string();
                    break;
                default:
                    let u = options.readUnknownField;
                    if (u === "throw")
                        throw new globalThis.Error(`Unknown field ${fieldNo} (wire type ${wireType}) for ${this.typeName}`);
                    let d = reader.skip(wireType);
                    if (u !== false)
                        (u === true ? UnknownFieldHandler.onRead : u)(this.typeName, message, fieldNo, wireType, d);
            }
        }
        return message;
    }
    internalBinaryWrite(message: GetJWTResponse, writer: IBinaryWriter, options: BinaryWriteOptions): IBinaryWriter {
        /* string token = 1; */
        if (message.token !== "")
            writer.tag(1, WireType.LengthDelimited).string(message.token);
        let u = options.writeUnknownFields;
        if (u !== false)
            (u == true ? UnknownFieldHandler.onWrite : u)(this.typeName, message, writer);
        return writer;
    }
}
/**
 * @generated MessageType for protobuf message auth.v1alpha1.GetJWTResponse
 */
export const GetJWTResponse = new GetJWTResponse$Type();
/**
 * @generated ServiceType for protobuf service auth.v1alpha1.AuthAPI
 */
export const AuthAPI = new ServiceType("auth.v1alpha1.AuthAPI", [
    { name: "GetJWT", options: {}, I: GetJWTRequest, O: GetJWTResponse }
]);