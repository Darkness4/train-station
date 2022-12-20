# Protocol Documentation
<a name="top"></a>

## Table of Contents

- [trainstation/v1alpha1/station.proto](#trainstation_v1alpha1_station-proto)
    - [Geometry](#trainstation-v1alpha1-Geometry)
    - [GetManyStationsRequest](#trainstation-v1alpha1-GetManyStationsRequest)
    - [GetManyStationsResponse](#trainstation-v1alpha1-GetManyStationsResponse)
    - [GetOneStationRequest](#trainstation-v1alpha1-GetOneStationRequest)
    - [GetOneStationResponse](#trainstation-v1alpha1-GetOneStationResponse)
    - [PaginatedStation](#trainstation-v1alpha1-PaginatedStation)
    - [SetFavoriteOneStationRequest](#trainstation-v1alpha1-SetFavoriteOneStationRequest)
    - [SetFavoriteOneStationResponse](#trainstation-v1alpha1-SetFavoriteOneStationResponse)
    - [Station](#trainstation-v1alpha1-Station)
  
    - [StationAPI](#trainstation-v1alpha1-StationAPI)
  
- [Scalar Value Types](#scalar-value-types)



<a name="trainstation_v1alpha1_station-proto"></a>
<p align="right"><a href="#top">Top</a></p>

## trainstation/v1alpha1/station.proto



<a name="trainstation-v1alpha1-Geometry"></a>

### Geometry



| Field | Type | Label | Description |
| ----- | ---- | ----- | ----------- |
| type | [string](#string) |  |  |
| coordinates | [double](#double) | repeated |  |






<a name="trainstation-v1alpha1-GetManyStationsRequest"></a>

### GetManyStationsRequest



| Field | Type | Label | Description |
| ----- | ---- | ----- | ----------- |
| query | [string](#string) |  |  |
| limit | [int64](#int64) |  |  |
| page | [int64](#int64) |  |  |
| token | [string](#string) |  |  |






<a name="trainstation-v1alpha1-GetManyStationsResponse"></a>

### GetManyStationsResponse



| Field | Type | Label | Description |
| ----- | ---- | ----- | ----------- |
| stations | [PaginatedStation](#trainstation-v1alpha1-PaginatedStation) |  |  |






<a name="trainstation-v1alpha1-GetOneStationRequest"></a>

### GetOneStationRequest



| Field | Type | Label | Description |
| ----- | ---- | ----- | ----------- |
| id | [string](#string) |  |  |
| token | [string](#string) |  |  |






<a name="trainstation-v1alpha1-GetOneStationResponse"></a>

### GetOneStationResponse



| Field | Type | Label | Description |
| ----- | ---- | ----- | ----------- |
| station | [Station](#trainstation-v1alpha1-Station) |  |  |






<a name="trainstation-v1alpha1-PaginatedStation"></a>

### PaginatedStation



| Field | Type | Label | Description |
| ----- | ---- | ----- | ----------- |
| data | [Station](#trainstation-v1alpha1-Station) | repeated |  |
| count | [int64](#int64) |  |  |
| total | [int64](#int64) |  |  |
| page | [int64](#int64) |  |  |
| page_count | [int64](#int64) |  |  |






<a name="trainstation-v1alpha1-SetFavoriteOneStationRequest"></a>

### SetFavoriteOneStationRequest



| Field | Type | Label | Description |
| ----- | ---- | ----- | ----------- |
| id | [string](#string) |  |  |
| token | [string](#string) |  |  |
| value | [bool](#bool) |  |  |






<a name="trainstation-v1alpha1-SetFavoriteOneStationResponse"></a>

### SetFavoriteOneStationResponse







<a name="trainstation-v1alpha1-Station"></a>

### Station



| Field | Type | Label | Description |
| ----- | ---- | ----- | ----------- |
| id | [string](#string) |  |  |
| commune | [string](#string) |  |  |
| y_wgs84 | [double](#double) |  |  |
| x_wgs84 | [double](#double) |  |  |
| libelle | [string](#string) |  |  |
| idgaia | [string](#string) |  |  |
| voyageurs | [string](#string) |  |  |
| geo_point_2d | [double](#double) | repeated |  |
| code_ligne | [string](#string) |  |  |
| x_l93 | [double](#double) |  |  |
| c_geo | [double](#double) | repeated |  |
| rg_troncon | [int64](#int64) |  |  |
| geo_shape | [Geometry](#trainstation-v1alpha1-Geometry) |  |  |
| pk | [string](#string) |  |  |
| idreseau | [int64](#int64) |  |  |
| departemen | [string](#string) |  |  |
| y_l93 | [double](#double) |  |  |
| fret | [string](#string) |  |  |
| is_favorite | [bool](#bool) |  |  |





 

 

 


<a name="trainstation-v1alpha1-StationAPI"></a>

### StationAPI
StationAPI handles train stations from the SNCF.

The API needs the user to be authenticated via the AuthAPI.

| Method Name | Request Type | Response Type | Description |
| ----------- | ------------ | ------------- | ------------|
| GetManyStations | [GetManyStationsRequest](#trainstation-v1alpha1-GetManyStationsRequest) | [GetManyStationsResponse](#trainstation-v1alpha1-GetManyStationsResponse) | GetManyStations fetch a paginated list of station. |
| GetOneStation | [GetOneStationRequest](#trainstation-v1alpha1-GetOneStationRequest) | [GetOneStationResponse](#trainstation-v1alpha1-GetOneStationResponse) | GetOneStation fetches the details of a station. |
| SetFavoriteOneStation | [SetFavoriteOneStationRequest](#trainstation-v1alpha1-SetFavoriteOneStationRequest) | [SetFavoriteOneStationResponse](#trainstation-v1alpha1-SetFavoriteOneStationResponse) | SetFavoriteOneStation set a station to favorite for a user. |

 



## Scalar Value Types

| .proto Type | Notes | C++ | Java | Python | Go | C# | PHP | Ruby |
| ----------- | ----- | --- | ---- | ------ | -- | -- | --- | ---- |
| <a name="double" /> double |  | double | double | float | float64 | double | float | Float |
| <a name="float" /> float |  | float | float | float | float32 | float | float | Float |
| <a name="int32" /> int32 | Uses variable-length encoding. Inefficient for encoding negative numbers – if your field is likely to have negative values, use sint32 instead. | int32 | int | int | int32 | int | integer | Bignum or Fixnum (as required) |
| <a name="int64" /> int64 | Uses variable-length encoding. Inefficient for encoding negative numbers – if your field is likely to have negative values, use sint64 instead. | int64 | long | int/long | int64 | long | integer/string | Bignum |
| <a name="uint32" /> uint32 | Uses variable-length encoding. | uint32 | int | int/long | uint32 | uint | integer | Bignum or Fixnum (as required) |
| <a name="uint64" /> uint64 | Uses variable-length encoding. | uint64 | long | int/long | uint64 | ulong | integer/string | Bignum or Fixnum (as required) |
| <a name="sint32" /> sint32 | Uses variable-length encoding. Signed int value. These more efficiently encode negative numbers than regular int32s. | int32 | int | int | int32 | int | integer | Bignum or Fixnum (as required) |
| <a name="sint64" /> sint64 | Uses variable-length encoding. Signed int value. These more efficiently encode negative numbers than regular int64s. | int64 | long | int/long | int64 | long | integer/string | Bignum |
| <a name="fixed32" /> fixed32 | Always four bytes. More efficient than uint32 if values are often greater than 2^28. | uint32 | int | int | uint32 | uint | integer | Bignum or Fixnum (as required) |
| <a name="fixed64" /> fixed64 | Always eight bytes. More efficient than uint64 if values are often greater than 2^56. | uint64 | long | int/long | uint64 | ulong | integer/string | Bignum |
| <a name="sfixed32" /> sfixed32 | Always four bytes. | int32 | int | int | int32 | int | integer | Bignum or Fixnum (as required) |
| <a name="sfixed64" /> sfixed64 | Always eight bytes. | int64 | long | int/long | int64 | long | integer/string | Bignum |
| <a name="bool" /> bool |  | bool | boolean | boolean | bool | bool | boolean | TrueClass/FalseClass |
| <a name="string" /> string | A string must always contain UTF-8 encoded or 7-bit ASCII text. | string | String | str/unicode | string | string | string | String (UTF-8) |
| <a name="bytes" /> bytes | May contain any arbitrary sequence of bytes. | string | ByteString | str | []byte | ByteString | string | String (ASCII-8BIT) |

