// Code generated by protoc-gen-go. DO NOT EDIT.
// versions:
// 	protoc-gen-go v1.26.0
// 	protoc        (unknown)
// source: auth/v1alpha1/auth.proto

package authv1alpha1

import (
	protoreflect "google.golang.org/protobuf/reflect/protoreflect"
	protoimpl "google.golang.org/protobuf/runtime/protoimpl"
	reflect "reflect"
	sync "sync"
)

const (
	// Verify that this generated code is sufficiently up-to-date.
	_ = protoimpl.EnforceVersion(20 - protoimpl.MinVersion)
	// Verify that runtime/protoimpl is sufficiently up-to-date.
	_ = protoimpl.EnforceVersion(protoimpl.MaxVersion - 20)
)

// Account are the credentials for authentication API.
type Account struct {
	state         protoimpl.MessageState
	sizeCache     protoimpl.SizeCache
	unknownFields protoimpl.UnknownFields

	// *
	// ID of the provider used for this account.
	//
	// Based on the ID of the provider, it will check the access token on the OAuth/OIDC Provider.
	Provider string `protobuf:"bytes,1,opt,name=provider,proto3" json:"provider,omitempty"`
	// * Provider's type for this account, oauth or oidc.
	Type string `protobuf:"bytes,2,opt,name=type,proto3" json:"type,omitempty"`
	// * The provider account ID.
	ProviderAccountId string `protobuf:"bytes,3,opt,name=provider_account_id,json=providerAccountId,proto3" json:"provider_account_id,omitempty"`
	// * The provider access_token.
	AccessToken string `protobuf:"bytes,4,opt,name=access_token,json=accessToken,proto3" json:"access_token,omitempty"`
}

func (x *Account) Reset() {
	*x = Account{}
	if protoimpl.UnsafeEnabled {
		mi := &file_auth_v1alpha1_auth_proto_msgTypes[0]
		ms := protoimpl.X.MessageStateOf(protoimpl.Pointer(x))
		ms.StoreMessageInfo(mi)
	}
}

func (x *Account) String() string {
	return protoimpl.X.MessageStringOf(x)
}

func (*Account) ProtoMessage() {}

func (x *Account) ProtoReflect() protoreflect.Message {
	mi := &file_auth_v1alpha1_auth_proto_msgTypes[0]
	if protoimpl.UnsafeEnabled && x != nil {
		ms := protoimpl.X.MessageStateOf(protoimpl.Pointer(x))
		if ms.LoadMessageInfo() == nil {
			ms.StoreMessageInfo(mi)
		}
		return ms
	}
	return mi.MessageOf(x)
}

// Deprecated: Use Account.ProtoReflect.Descriptor instead.
func (*Account) Descriptor() ([]byte, []int) {
	return file_auth_v1alpha1_auth_proto_rawDescGZIP(), []int{0}
}

func (x *Account) GetProvider() string {
	if x != nil {
		return x.Provider
	}
	return ""
}

func (x *Account) GetType() string {
	if x != nil {
		return x.Type
	}
	return ""
}

func (x *Account) GetProviderAccountId() string {
	if x != nil {
		return x.ProviderAccountId
	}
	return ""
}

func (x *Account) GetAccessToken() string {
	if x != nil {
		return x.AccessToken
	}
	return ""
}

type GetJWTRequest struct {
	state         protoimpl.MessageState
	sizeCache     protoimpl.SizeCache
	unknownFields protoimpl.UnknownFields

	Account *Account `protobuf:"bytes,1,opt,name=account,proto3" json:"account,omitempty"`
}

func (x *GetJWTRequest) Reset() {
	*x = GetJWTRequest{}
	if protoimpl.UnsafeEnabled {
		mi := &file_auth_v1alpha1_auth_proto_msgTypes[1]
		ms := protoimpl.X.MessageStateOf(protoimpl.Pointer(x))
		ms.StoreMessageInfo(mi)
	}
}

func (x *GetJWTRequest) String() string {
	return protoimpl.X.MessageStringOf(x)
}

func (*GetJWTRequest) ProtoMessage() {}

func (x *GetJWTRequest) ProtoReflect() protoreflect.Message {
	mi := &file_auth_v1alpha1_auth_proto_msgTypes[1]
	if protoimpl.UnsafeEnabled && x != nil {
		ms := protoimpl.X.MessageStateOf(protoimpl.Pointer(x))
		if ms.LoadMessageInfo() == nil {
			ms.StoreMessageInfo(mi)
		}
		return ms
	}
	return mi.MessageOf(x)
}

// Deprecated: Use GetJWTRequest.ProtoReflect.Descriptor instead.
func (*GetJWTRequest) Descriptor() ([]byte, []int) {
	return file_auth_v1alpha1_auth_proto_rawDescGZIP(), []int{1}
}

func (x *GetJWTRequest) GetAccount() *Account {
	if x != nil {
		return x.Account
	}
	return nil
}

type GetJWTResponse struct {
	state         protoimpl.MessageState
	sizeCache     protoimpl.SizeCache
	unknownFields protoimpl.UnknownFields

	Token string `protobuf:"bytes,1,opt,name=token,proto3" json:"token,omitempty"`
}

func (x *GetJWTResponse) Reset() {
	*x = GetJWTResponse{}
	if protoimpl.UnsafeEnabled {
		mi := &file_auth_v1alpha1_auth_proto_msgTypes[2]
		ms := protoimpl.X.MessageStateOf(protoimpl.Pointer(x))
		ms.StoreMessageInfo(mi)
	}
}

func (x *GetJWTResponse) String() string {
	return protoimpl.X.MessageStringOf(x)
}

func (*GetJWTResponse) ProtoMessage() {}

func (x *GetJWTResponse) ProtoReflect() protoreflect.Message {
	mi := &file_auth_v1alpha1_auth_proto_msgTypes[2]
	if protoimpl.UnsafeEnabled && x != nil {
		ms := protoimpl.X.MessageStateOf(protoimpl.Pointer(x))
		if ms.LoadMessageInfo() == nil {
			ms.StoreMessageInfo(mi)
		}
		return ms
	}
	return mi.MessageOf(x)
}

// Deprecated: Use GetJWTResponse.ProtoReflect.Descriptor instead.
func (*GetJWTResponse) Descriptor() ([]byte, []int) {
	return file_auth_v1alpha1_auth_proto_rawDescGZIP(), []int{2}
}

func (x *GetJWTResponse) GetToken() string {
	if x != nil {
		return x.Token
	}
	return ""
}

var File_auth_v1alpha1_auth_proto protoreflect.FileDescriptor

var file_auth_v1alpha1_auth_proto_rawDesc = []byte{
	0x0a, 0x18, 0x61, 0x75, 0x74, 0x68, 0x2f, 0x76, 0x31, 0x61, 0x6c, 0x70, 0x68, 0x61, 0x31, 0x2f,
	0x61, 0x75, 0x74, 0x68, 0x2e, 0x70, 0x72, 0x6f, 0x74, 0x6f, 0x12, 0x0d, 0x61, 0x75, 0x74, 0x68,
	0x2e, 0x76, 0x31, 0x61, 0x6c, 0x70, 0x68, 0x61, 0x31, 0x22, 0x8c, 0x01, 0x0a, 0x07, 0x41, 0x63,
	0x63, 0x6f, 0x75, 0x6e, 0x74, 0x12, 0x1a, 0x0a, 0x08, 0x70, 0x72, 0x6f, 0x76, 0x69, 0x64, 0x65,
	0x72, 0x18, 0x01, 0x20, 0x01, 0x28, 0x09, 0x52, 0x08, 0x70, 0x72, 0x6f, 0x76, 0x69, 0x64, 0x65,
	0x72, 0x12, 0x12, 0x0a, 0x04, 0x74, 0x79, 0x70, 0x65, 0x18, 0x02, 0x20, 0x01, 0x28, 0x09, 0x52,
	0x04, 0x74, 0x79, 0x70, 0x65, 0x12, 0x2e, 0x0a, 0x13, 0x70, 0x72, 0x6f, 0x76, 0x69, 0x64, 0x65,
	0x72, 0x5f, 0x61, 0x63, 0x63, 0x6f, 0x75, 0x6e, 0x74, 0x5f, 0x69, 0x64, 0x18, 0x03, 0x20, 0x01,
	0x28, 0x09, 0x52, 0x11, 0x70, 0x72, 0x6f, 0x76, 0x69, 0x64, 0x65, 0x72, 0x41, 0x63, 0x63, 0x6f,
	0x75, 0x6e, 0x74, 0x49, 0x64, 0x12, 0x21, 0x0a, 0x0c, 0x61, 0x63, 0x63, 0x65, 0x73, 0x73, 0x5f,
	0x74, 0x6f, 0x6b, 0x65, 0x6e, 0x18, 0x04, 0x20, 0x01, 0x28, 0x09, 0x52, 0x0b, 0x61, 0x63, 0x63,
	0x65, 0x73, 0x73, 0x54, 0x6f, 0x6b, 0x65, 0x6e, 0x22, 0x41, 0x0a, 0x0d, 0x47, 0x65, 0x74, 0x4a,
	0x57, 0x54, 0x52, 0x65, 0x71, 0x75, 0x65, 0x73, 0x74, 0x12, 0x30, 0x0a, 0x07, 0x61, 0x63, 0x63,
	0x6f, 0x75, 0x6e, 0x74, 0x18, 0x01, 0x20, 0x01, 0x28, 0x0b, 0x32, 0x16, 0x2e, 0x61, 0x75, 0x74,
	0x68, 0x2e, 0x76, 0x31, 0x61, 0x6c, 0x70, 0x68, 0x61, 0x31, 0x2e, 0x41, 0x63, 0x63, 0x6f, 0x75,
	0x6e, 0x74, 0x52, 0x07, 0x61, 0x63, 0x63, 0x6f, 0x75, 0x6e, 0x74, 0x22, 0x26, 0x0a, 0x0e, 0x47,
	0x65, 0x74, 0x4a, 0x57, 0x54, 0x52, 0x65, 0x73, 0x70, 0x6f, 0x6e, 0x73, 0x65, 0x12, 0x14, 0x0a,
	0x05, 0x74, 0x6f, 0x6b, 0x65, 0x6e, 0x18, 0x01, 0x20, 0x01, 0x28, 0x09, 0x52, 0x05, 0x74, 0x6f,
	0x6b, 0x65, 0x6e, 0x32, 0x52, 0x0a, 0x07, 0x41, 0x75, 0x74, 0x68, 0x41, 0x50, 0x49, 0x12, 0x47,
	0x0a, 0x06, 0x47, 0x65, 0x74, 0x4a, 0x57, 0x54, 0x12, 0x1c, 0x2e, 0x61, 0x75, 0x74, 0x68, 0x2e,
	0x76, 0x31, 0x61, 0x6c, 0x70, 0x68, 0x61, 0x31, 0x2e, 0x47, 0x65, 0x74, 0x4a, 0x57, 0x54, 0x52,
	0x65, 0x71, 0x75, 0x65, 0x73, 0x74, 0x1a, 0x1d, 0x2e, 0x61, 0x75, 0x74, 0x68, 0x2e, 0x76, 0x31,
	0x61, 0x6c, 0x70, 0x68, 0x61, 0x31, 0x2e, 0x47, 0x65, 0x74, 0x4a, 0x57, 0x54, 0x52, 0x65, 0x73,
	0x70, 0x6f, 0x6e, 0x73, 0x65, 0x22, 0x00, 0x42, 0xf4, 0x01, 0x0a, 0x33, 0x63, 0x6f, 0x6d, 0x2e,
	0x65, 0x78, 0x61, 0x6d, 0x70, 0x6c, 0x65, 0x2e, 0x74, 0x72, 0x61, 0x69, 0x6e, 0x73, 0x74, 0x61,
	0x74, 0x69, 0x6f, 0x6e, 0x61, 0x70, 0x70, 0x2e, 0x64, 0x61, 0x74, 0x61, 0x2e, 0x67, 0x72, 0x70,
	0x63, 0x2e, 0x61, 0x75, 0x74, 0x68, 0x2e, 0x76, 0x31, 0x61, 0x6c, 0x70, 0x68, 0x61, 0x31, 0x42,
	0x09, 0x41, 0x75, 0x74, 0x68, 0x50, 0x72, 0x6f, 0x74, 0x6f, 0x48, 0x03, 0x5a, 0x5a, 0x67, 0x69,
	0x74, 0x68, 0x75, 0x62, 0x2e, 0x63, 0x6f, 0x6d, 0x2f, 0x44, 0x61, 0x72, 0x6b, 0x6e, 0x65, 0x73,
	0x73, 0x34, 0x2f, 0x74, 0x72, 0x61, 0x69, 0x6e, 0x2d, 0x73, 0x74, 0x61, 0x74, 0x69, 0x6f, 0x6e,
	0x2f, 0x74, 0x72, 0x61, 0x69, 0x6e, 0x73, 0x74, 0x61, 0x74, 0x69, 0x6f, 0x6e, 0x2f, 0x76, 0x31,
	0x61, 0x6c, 0x70, 0x68, 0x61, 0x31, 0x2f, 0x67, 0x65, 0x6e, 0x2f, 0x67, 0x6f, 0x2f, 0x61, 0x75,
	0x74, 0x68, 0x2f, 0x76, 0x31, 0x61, 0x6c, 0x70, 0x68, 0x61, 0x31, 0x3b, 0x61, 0x75, 0x74, 0x68,
	0x76, 0x31, 0x61, 0x6c, 0x70, 0x68, 0x61, 0x31, 0xf8, 0x01, 0x00, 0xa2, 0x02, 0x03, 0x41, 0x58,
	0x58, 0xaa, 0x02, 0x0d, 0x41, 0x75, 0x74, 0x68, 0x2e, 0x56, 0x31, 0x61, 0x6c, 0x70, 0x68, 0x61,
	0x31, 0xca, 0x02, 0x0d, 0x41, 0x75, 0x74, 0x68, 0x5c, 0x56, 0x31, 0x61, 0x6c, 0x70, 0x68, 0x61,
	0x31, 0xe2, 0x02, 0x19, 0x41, 0x75, 0x74, 0x68, 0x5c, 0x56, 0x31, 0x61, 0x6c, 0x70, 0x68, 0x61,
	0x31, 0x5c, 0x47, 0x50, 0x42, 0x4d, 0x65, 0x74, 0x61, 0x64, 0x61, 0x74, 0x61, 0xea, 0x02, 0x0e,
	0x41, 0x75, 0x74, 0x68, 0x3a, 0x3a, 0x56, 0x31, 0x61, 0x6c, 0x70, 0x68, 0x61, 0x31, 0x62, 0x06,
	0x70, 0x72, 0x6f, 0x74, 0x6f, 0x33,
}

var (
	file_auth_v1alpha1_auth_proto_rawDescOnce sync.Once
	file_auth_v1alpha1_auth_proto_rawDescData = file_auth_v1alpha1_auth_proto_rawDesc
)

func file_auth_v1alpha1_auth_proto_rawDescGZIP() []byte {
	file_auth_v1alpha1_auth_proto_rawDescOnce.Do(func() {
		file_auth_v1alpha1_auth_proto_rawDescData = protoimpl.X.CompressGZIP(file_auth_v1alpha1_auth_proto_rawDescData)
	})
	return file_auth_v1alpha1_auth_proto_rawDescData
}

var file_auth_v1alpha1_auth_proto_msgTypes = make([]protoimpl.MessageInfo, 3)
var file_auth_v1alpha1_auth_proto_goTypes = []interface{}{
	(*Account)(nil),        // 0: auth.v1alpha1.Account
	(*GetJWTRequest)(nil),  // 1: auth.v1alpha1.GetJWTRequest
	(*GetJWTResponse)(nil), // 2: auth.v1alpha1.GetJWTResponse
}
var file_auth_v1alpha1_auth_proto_depIdxs = []int32{
	0, // 0: auth.v1alpha1.GetJWTRequest.account:type_name -> auth.v1alpha1.Account
	1, // 1: auth.v1alpha1.AuthAPI.GetJWT:input_type -> auth.v1alpha1.GetJWTRequest
	2, // 2: auth.v1alpha1.AuthAPI.GetJWT:output_type -> auth.v1alpha1.GetJWTResponse
	2, // [2:3] is the sub-list for method output_type
	1, // [1:2] is the sub-list for method input_type
	1, // [1:1] is the sub-list for extension type_name
	1, // [1:1] is the sub-list for extension extendee
	0, // [0:1] is the sub-list for field type_name
}

func init() { file_auth_v1alpha1_auth_proto_init() }
func file_auth_v1alpha1_auth_proto_init() {
	if File_auth_v1alpha1_auth_proto != nil {
		return
	}
	if !protoimpl.UnsafeEnabled {
		file_auth_v1alpha1_auth_proto_msgTypes[0].Exporter = func(v interface{}, i int) interface{} {
			switch v := v.(*Account); i {
			case 0:
				return &v.state
			case 1:
				return &v.sizeCache
			case 2:
				return &v.unknownFields
			default:
				return nil
			}
		}
		file_auth_v1alpha1_auth_proto_msgTypes[1].Exporter = func(v interface{}, i int) interface{} {
			switch v := v.(*GetJWTRequest); i {
			case 0:
				return &v.state
			case 1:
				return &v.sizeCache
			case 2:
				return &v.unknownFields
			default:
				return nil
			}
		}
		file_auth_v1alpha1_auth_proto_msgTypes[2].Exporter = func(v interface{}, i int) interface{} {
			switch v := v.(*GetJWTResponse); i {
			case 0:
				return &v.state
			case 1:
				return &v.sizeCache
			case 2:
				return &v.unknownFields
			default:
				return nil
			}
		}
	}
	type x struct{}
	out := protoimpl.TypeBuilder{
		File: protoimpl.DescBuilder{
			GoPackagePath: reflect.TypeOf(x{}).PkgPath(),
			RawDescriptor: file_auth_v1alpha1_auth_proto_rawDesc,
			NumEnums:      0,
			NumMessages:   3,
			NumExtensions: 0,
			NumServices:   1,
		},
		GoTypes:           file_auth_v1alpha1_auth_proto_goTypes,
		DependencyIndexes: file_auth_v1alpha1_auth_proto_depIdxs,
		MessageInfos:      file_auth_v1alpha1_auth_proto_msgTypes,
	}.Build()
	File_auth_v1alpha1_auth_proto = out.File
	file_auth_v1alpha1_auth_proto_rawDesc = nil
	file_auth_v1alpha1_auth_proto_goTypes = nil
	file_auth_v1alpha1_auth_proto_depIdxs = nil
}
