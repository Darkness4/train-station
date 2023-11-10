// @generated by protobuf-ts 2.9.1
// @generated from protobuf file "trainstation/v1alpha1/station.proto" (package "trainstation.v1alpha1", syntax proto3)
// tslint:disable
import { ServiceType } from "@protobuf-ts/runtime-rpc";
import { MessageType } from "@protobuf-ts/runtime";
/**
 * @generated from protobuf message trainstation.v1alpha1.GetManyStationsRequest
 */
export interface GetManyStationsRequest {
    /**
     * @generated from protobuf field: string query = 1;
     */
    query: string;
    /**
     * @generated from protobuf field: int64 limit = 2 [jstype = JS_NUMBER];
     */
    limit: number;
    /**
     * @generated from protobuf field: int64 page = 3 [jstype = JS_NUMBER];
     */
    page: number;
    /**
     * @generated from protobuf field: string token = 4;
     */
    token: string;
}
/**
 * @generated from protobuf message trainstation.v1alpha1.GetManyStationsResponse
 */
export interface GetManyStationsResponse {
    /**
     * @generated from protobuf field: trainstation.v1alpha1.PaginatedStation stations = 1;
     */
    stations?: PaginatedStation;
}
/**
 * @generated from protobuf message trainstation.v1alpha1.GetOneStationRequest
 */
export interface GetOneStationRequest {
    /**
     * @generated from protobuf field: string id = 1;
     */
    id: string;
    /**
     * @generated from protobuf field: string token = 2;
     */
    token: string;
}
/**
 * @generated from protobuf message trainstation.v1alpha1.GetOneStationResponse
 */
export interface GetOneStationResponse {
    /**
     * @generated from protobuf field: trainstation.v1alpha1.Station station = 1;
     */
    station?: Station;
}
/**
 * @generated from protobuf message trainstation.v1alpha1.SetFavoriteOneStationRequest
 */
export interface SetFavoriteOneStationRequest {
    /**
     * @generated from protobuf field: string id = 1;
     */
    id: string;
    /**
     * @generated from protobuf field: string token = 2;
     */
    token: string;
    /**
     * @generated from protobuf field: bool value = 3;
     */
    value: boolean;
}
/**
 * @generated from protobuf message trainstation.v1alpha1.SetFavoriteOneStationResponse
 */
export interface SetFavoriteOneStationResponse {
}
/**
 * @generated from protobuf message trainstation.v1alpha1.PaginatedStation
 */
export interface PaginatedStation {
    /**
     * @generated from protobuf field: repeated trainstation.v1alpha1.Station data = 1;
     */
    data: Station[];
    /**
     * @generated from protobuf field: int64 count = 2 [jstype = JS_NUMBER];
     */
    count: number;
    /**
     * @generated from protobuf field: int64 total = 3 [jstype = JS_NUMBER];
     */
    total: number;
    /**
     * @generated from protobuf field: int64 page = 4 [jstype = JS_NUMBER];
     */
    page: number;
    /**
     * @generated from protobuf field: int64 page_count = 5 [jstype = JS_NUMBER];
     */
    pageCount: number;
}
/**
 * @generated from protobuf message trainstation.v1alpha1.Station
 */
export interface Station {
    /**
     * @generated from protobuf field: string id = 1;
     */
    id: string;
    /**
     * @generated from protobuf field: string commune = 2;
     */
    commune: string;
    /**
     * @generated from protobuf field: double y_wgs84 = 3;
     */
    yWgs84: number;
    /**
     * @generated from protobuf field: double x_wgs84 = 4;
     */
    xWgs84: number;
    /**
     * @generated from protobuf field: string libelle = 5;
     */
    libelle: string;
    /**
     * @generated from protobuf field: string idgaia = 6;
     */
    idgaia: string;
    /**
     * @generated from protobuf field: string voyageurs = 7;
     */
    voyageurs: string;
    /**
     * @generated from protobuf field: repeated double geo_point_2d = 8 [json_name = "geoPoint2d"];
     */
    geoPoint2D: number[];
    /**
     * @generated from protobuf field: string code_ligne = 9;
     */
    codeLigne: string;
    /**
     * @generated from protobuf field: double x_l93 = 10;
     */
    xL93: number;
    /**
     * @generated from protobuf field: repeated double c_geo = 11;
     */
    cGeo: number[];
    /**
     * @generated from protobuf field: int64 rg_troncon = 12 [jstype = JS_NUMBER];
     */
    rgTroncon: number;
    /**
     * @generated from protobuf field: trainstation.v1alpha1.Geometry geo_shape = 13;
     */
    geoShape?: Geometry;
    /**
     * @generated from protobuf field: string pk = 14;
     */
    pk: string;
    /**
     * @generated from protobuf field: int64 idreseau = 15 [jstype = JS_NUMBER];
     */
    idreseau: number;
    /**
     * @generated from protobuf field: string departemen = 16;
     */
    departemen: string;
    /**
     * @generated from protobuf field: double y_l93 = 17;
     */
    yL93: number;
    /**
     * @generated from protobuf field: string fret = 18;
     */
    fret: string;
    /**
     * @generated from protobuf field: bool is_favorite = 19;
     */
    isFavorite: boolean;
}
/**
 * @generated from protobuf message trainstation.v1alpha1.Geometry
 */
export interface Geometry {
    /**
     * @generated from protobuf field: string type = 1;
     */
    type: string;
    /**
     * @generated from protobuf field: repeated double coordinates = 2;
     */
    coordinates: number[];
}
// @generated message type with reflection information, may provide speed optimized methods
class GetManyStationsRequest$Type extends MessageType<GetManyStationsRequest> {
    constructor() {
        super("trainstation.v1alpha1.GetManyStationsRequest", [
            { no: 1, name: "query", kind: "scalar", T: 9 /*ScalarType.STRING*/ },
            { no: 2, name: "limit", kind: "scalar", T: 3 /*ScalarType.INT64*/, L: 2 /*LongType.NUMBER*/ },
            { no: 3, name: "page", kind: "scalar", T: 3 /*ScalarType.INT64*/, L: 2 /*LongType.NUMBER*/ },
            { no: 4, name: "token", kind: "scalar", T: 9 /*ScalarType.STRING*/ }
        ]);
    }
}
/**
 * @generated MessageType for protobuf message trainstation.v1alpha1.GetManyStationsRequest
 */
export const GetManyStationsRequest = new GetManyStationsRequest$Type();
// @generated message type with reflection information, may provide speed optimized methods
class GetManyStationsResponse$Type extends MessageType<GetManyStationsResponse> {
    constructor() {
        super("trainstation.v1alpha1.GetManyStationsResponse", [
            { no: 1, name: "stations", kind: "message", T: () => PaginatedStation }
        ]);
    }
}
/**
 * @generated MessageType for protobuf message trainstation.v1alpha1.GetManyStationsResponse
 */
export const GetManyStationsResponse = new GetManyStationsResponse$Type();
// @generated message type with reflection information, may provide speed optimized methods
class GetOneStationRequest$Type extends MessageType<GetOneStationRequest> {
    constructor() {
        super("trainstation.v1alpha1.GetOneStationRequest", [
            { no: 1, name: "id", kind: "scalar", T: 9 /*ScalarType.STRING*/ },
            { no: 2, name: "token", kind: "scalar", T: 9 /*ScalarType.STRING*/ }
        ]);
    }
}
/**
 * @generated MessageType for protobuf message trainstation.v1alpha1.GetOneStationRequest
 */
export const GetOneStationRequest = new GetOneStationRequest$Type();
// @generated message type with reflection information, may provide speed optimized methods
class GetOneStationResponse$Type extends MessageType<GetOneStationResponse> {
    constructor() {
        super("trainstation.v1alpha1.GetOneStationResponse", [
            { no: 1, name: "station", kind: "message", T: () => Station }
        ]);
    }
}
/**
 * @generated MessageType for protobuf message trainstation.v1alpha1.GetOneStationResponse
 */
export const GetOneStationResponse = new GetOneStationResponse$Type();
// @generated message type with reflection information, may provide speed optimized methods
class SetFavoriteOneStationRequest$Type extends MessageType<SetFavoriteOneStationRequest> {
    constructor() {
        super("trainstation.v1alpha1.SetFavoriteOneStationRequest", [
            { no: 1, name: "id", kind: "scalar", T: 9 /*ScalarType.STRING*/ },
            { no: 2, name: "token", kind: "scalar", T: 9 /*ScalarType.STRING*/ },
            { no: 3, name: "value", kind: "scalar", T: 8 /*ScalarType.BOOL*/ }
        ]);
    }
}
/**
 * @generated MessageType for protobuf message trainstation.v1alpha1.SetFavoriteOneStationRequest
 */
export const SetFavoriteOneStationRequest = new SetFavoriteOneStationRequest$Type();
// @generated message type with reflection information, may provide speed optimized methods
class SetFavoriteOneStationResponse$Type extends MessageType<SetFavoriteOneStationResponse> {
    constructor() {
        super("trainstation.v1alpha1.SetFavoriteOneStationResponse", []);
    }
}
/**
 * @generated MessageType for protobuf message trainstation.v1alpha1.SetFavoriteOneStationResponse
 */
export const SetFavoriteOneStationResponse = new SetFavoriteOneStationResponse$Type();
// @generated message type with reflection information, may provide speed optimized methods
class PaginatedStation$Type extends MessageType<PaginatedStation> {
    constructor() {
        super("trainstation.v1alpha1.PaginatedStation", [
            { no: 1, name: "data", kind: "message", repeat: 1 /*RepeatType.PACKED*/, T: () => Station },
            { no: 2, name: "count", kind: "scalar", T: 3 /*ScalarType.INT64*/, L: 2 /*LongType.NUMBER*/ },
            { no: 3, name: "total", kind: "scalar", T: 3 /*ScalarType.INT64*/, L: 2 /*LongType.NUMBER*/ },
            { no: 4, name: "page", kind: "scalar", T: 3 /*ScalarType.INT64*/, L: 2 /*LongType.NUMBER*/ },
            { no: 5, name: "page_count", kind: "scalar", T: 3 /*ScalarType.INT64*/, L: 2 /*LongType.NUMBER*/ }
        ]);
    }
}
/**
 * @generated MessageType for protobuf message trainstation.v1alpha1.PaginatedStation
 */
export const PaginatedStation = new PaginatedStation$Type();
// @generated message type with reflection information, may provide speed optimized methods
class Station$Type extends MessageType<Station> {
    constructor() {
        super("trainstation.v1alpha1.Station", [
            { no: 1, name: "id", kind: "scalar", T: 9 /*ScalarType.STRING*/ },
            { no: 2, name: "commune", kind: "scalar", T: 9 /*ScalarType.STRING*/ },
            { no: 3, name: "y_wgs84", kind: "scalar", T: 1 /*ScalarType.DOUBLE*/ },
            { no: 4, name: "x_wgs84", kind: "scalar", T: 1 /*ScalarType.DOUBLE*/ },
            { no: 5, name: "libelle", kind: "scalar", T: 9 /*ScalarType.STRING*/ },
            { no: 6, name: "idgaia", kind: "scalar", T: 9 /*ScalarType.STRING*/ },
            { no: 7, name: "voyageurs", kind: "scalar", T: 9 /*ScalarType.STRING*/ },
            { no: 8, name: "geo_point_2d", kind: "scalar", jsonName: "geoPoint2d", repeat: 1 /*RepeatType.PACKED*/, T: 1 /*ScalarType.DOUBLE*/ },
            { no: 9, name: "code_ligne", kind: "scalar", T: 9 /*ScalarType.STRING*/ },
            { no: 10, name: "x_l93", kind: "scalar", T: 1 /*ScalarType.DOUBLE*/ },
            { no: 11, name: "c_geo", kind: "scalar", repeat: 1 /*RepeatType.PACKED*/, T: 1 /*ScalarType.DOUBLE*/ },
            { no: 12, name: "rg_troncon", kind: "scalar", T: 3 /*ScalarType.INT64*/, L: 2 /*LongType.NUMBER*/ },
            { no: 13, name: "geo_shape", kind: "message", T: () => Geometry },
            { no: 14, name: "pk", kind: "scalar", T: 9 /*ScalarType.STRING*/ },
            { no: 15, name: "idreseau", kind: "scalar", T: 3 /*ScalarType.INT64*/, L: 2 /*LongType.NUMBER*/ },
            { no: 16, name: "departemen", kind: "scalar", T: 9 /*ScalarType.STRING*/ },
            { no: 17, name: "y_l93", kind: "scalar", T: 1 /*ScalarType.DOUBLE*/ },
            { no: 18, name: "fret", kind: "scalar", T: 9 /*ScalarType.STRING*/ },
            { no: 19, name: "is_favorite", kind: "scalar", T: 8 /*ScalarType.BOOL*/ }
        ]);
    }
}
/**
 * @generated MessageType for protobuf message trainstation.v1alpha1.Station
 */
export const Station = new Station$Type();
// @generated message type with reflection information, may provide speed optimized methods
class Geometry$Type extends MessageType<Geometry> {
    constructor() {
        super("trainstation.v1alpha1.Geometry", [
            { no: 1, name: "type", kind: "scalar", T: 9 /*ScalarType.STRING*/ },
            { no: 2, name: "coordinates", kind: "scalar", repeat: 1 /*RepeatType.PACKED*/, T: 1 /*ScalarType.DOUBLE*/ }
        ]);
    }
}
/**
 * @generated MessageType for protobuf message trainstation.v1alpha1.Geometry
 */
export const Geometry = new Geometry$Type();
/**
 * @generated ServiceType for protobuf service trainstation.v1alpha1.StationAPI
 */
export const StationAPI = new ServiceType("trainstation.v1alpha1.StationAPI", [
    { name: "GetManyStations", options: {}, I: GetManyStationsRequest, O: GetManyStationsResponse },
    { name: "GetOneStation", options: {}, I: GetOneStationRequest, O: GetOneStationResponse },
    { name: "SetFavoriteOneStation", options: {}, I: SetFavoriteOneStationRequest, O: SetFavoriteOneStationResponse }
]);
