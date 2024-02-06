//go:build tools

package tools

import (
	_ "github.com/golang-migrate/migrate/v4/cmd/migrate"
	_ "github.com/sqlc-dev/sqlc/cmd/sqlc"
	_ "google.golang.org/grpc/cmd/protoc-gen-go-grpc"
	_ "google.golang.org/protobuf/cmd/protoc-gen-go"
)
