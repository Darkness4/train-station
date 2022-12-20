# Protocol Documentation
<a name="top"></a>

## Table of Contents

- [auth/v1alpha1/auth.proto](#auth_v1alpha1_auth-proto)
    - [Account](#auth-v1alpha1-Account)
    - [GetJWTRequest](#auth-v1alpha1-GetJWTRequest)
    - [GetJWTResponse](#auth-v1alpha1-GetJWTResponse)
  
    - [AuthAPI](#auth-v1alpha1-AuthAPI)
  
- [Scalar Value Types](#scalar-value-types)



<a name="auth_v1alpha1_auth-proto"></a>
<p align="right"><a href="#top">Top</a></p>

## auth/v1alpha1/auth.proto



<a name="auth-v1alpha1-Account"></a>

### Account
Account are the credentials for authentication API.


| Field | Type | Label | Description |
| ----- | ---- | ----- | ----------- |
| provider | [string](#string) |  | ID of the provider used for this account.

Based on the ID of the provider, it will check the access token on the OAuth/OIDC Provider. |
| type | [string](#string) |  | Provider&#39;s type for this account, oauth or oidc. |
| provider_account_id | [string](#string) |  | The provider account ID. |
| access_token | [string](#string) |  | The provider access_token. |






<a name="auth-v1alpha1-GetJWTRequest"></a>

### GetJWTRequest



| Field | Type | Label | Description |
| ----- | ---- | ----- | ----------- |
| account | [Account](#auth-v1alpha1-Account) |  |  |






<a name="auth-v1alpha1-GetJWTResponse"></a>

### GetJWTResponse



| Field | Type | Label | Description |
| ----- | ---- | ----- | ----------- |
| token | [string](#string) |  |  |





 

 

 


<a name="auth-v1alpha1-AuthAPI"></a>

### AuthAPI
AuthAPI is the main authentication API between the backend and the frontends.

| Method Name | Request Type | Response Type | Description |
| ----------- | ------------ | ------------- | ------------|
| GetJWT | [GetJWTRequest](#auth-v1alpha1-GetJWTRequest) | [GetJWTResponse](#auth-v1alpha1-GetJWTResponse) |  |

 



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

