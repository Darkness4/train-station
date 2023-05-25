GO_SRCS := $(shell find . -type f -name '*.go' -a -name '*.tpl' -a ! \( -name 'zz_generated*' -o -name '*_test.go' \))
GO_TESTS := $(shell find . -type f -name '*_test.go')
TAG_NAME = $(shell git describe --tags --abbrev=0 --exact-match 2>/dev/null)
TAG_NAME_DEV = $(shell git describe --tags --abbrev=0 2>/dev/null)
VERSION_CORE = $(shell echo $(TAG_NAME) | sed 's/^\(v[0-9]\+\.[0-9]\+\.[0-9]\+\)\(+\([0-9]\+\)\)\?$$/\1/')
VERSION_CORE_DEV = $(shell echo $(TAG_NAME_DEV) | sed 's/^\(v[0-9]\+\.[0-9]\+\.[0-9]\+\)\(+\([0-9]\+\)\)\?$$/\1/')
GIT_COMMIT = $(shell git rev-parse --short=7 HEAD)
VERSION = $(or $(and $(TAG_NAME),$(VERSION_CORE)),$(and $(TAG_NAME_DEV),$(VERSION_CORE_DEV)-dev),$(GIT_COMMIT))
PROTOS := $(shell find ../protos -type f -name '*.proto')
MIGRATIONS := $(shell find ./db/migrations -type f -name '*.sql')

MIGRATION_NAME?=migration_name

bins := train-station-api-linux-amd64 train-station-api-linux-arm64 train-station-api-linux-mips64 train-station-api-linux-mips64le train-station-api-linux-ppc64 train-station-api-linux-ppc64le train-station-api-linux-riscv64 train-station-api-linux-s390x

.PHONY: bin/train-station-api
bin/train-station-api: $(GO_SRCS)
	CGO_ENABLED=0 go build -ldflags '-X main.version=${VERSION} -s -w' -o "$@" ./main.go

bin/train-station-api-linux-amd64: $(GO_SRCS)
	CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build -ldflags '-X main.version=${VERSION} -s -w' -o "$@" ./main.go

bin/train-station-api-linux-arm64: $(GO_SRCS)
	CGO_ENABLED=0 GOOS=linux GOARCH=arm64 go build -ldflags '-X main.version=${VERSION} -s -w' -o "$@" ./main.go

bin/train-station-api-linux-mips64: $(GO_SRCS)
	CGO_ENABLED=0 GOOS=linux GOARCH=mips64 go build -ldflags '-X main.version=${VERSION} -s -w' -o "$@" ./main.go

bin/train-station-api-linux-mips64le: $(GO_SRCS)
	CGO_ENABLED=0 GOOS=linux GOARCH=mips64le go build -ldflags '-X main.version=${VERSION} -s -w' -o "$@" ./main.go

bin/train-station-api-linux-ppc64: $(GO_SRCS)
	CGO_ENABLED=0 GOOS=linux GOARCH=ppc64 go build -ldflags '-X main.version=${VERSION} -s -w' -o "$@" ./main.go

bin/train-station-api-linux-ppc64le: $(GO_SRCS)
	CGO_ENABLED=0 GOOS=linux GOARCH=ppc64le go build -ldflags '-X main.version=${VERSION} -s -w' -o "$@" ./main.go

bin/train-station-api-linux-riscv64: $(GO_SRCS)
	CGO_ENABLED=0 GOOS=linux GOARCH=riscv64 go build -ldflags '-X main.version=${VERSION} -s -w' -o "$@" ./main.go

bin/train-station-api-linux-s390x: $(GO_SRCS)
	CGO_ENABLED=0 GOOS=linux GOARCH=s390x go build -ldflags '-X main.version=${VERSION} -s -w' -o "$@" ./main.go


ifeq ($(migrate),)
migrate := $(shell go env GOPATH)/bin/migrate
endif

ifeq ($(sqlboiler),)
sqlboiler := $(shell go env GOPATH)/bin/sqlboiler
endif

ifeq ($(golint),)
golint := $(shell go env GOPATH)/bin/golangci-lint
endif

bin/checksums.txt: $(addprefix bin/,$(bins))
	sha256sum -b $(addprefix bin/,$(bins)) | sed 's/bin\///' > $@

bin/checksums.md: bin/checksums.txt
	@echo "### SHA256 Checksums" > $@
	@echo >> $@
	@echo "\`\`\`" >> $@
	@cat $< >> $@
	@echo "\`\`\`" >> $@

.PHONY: build-all
build-all: $(addprefix bin/,$(bins)) bin/checksums.md

.PHONY: unit
unit: $(GO_TESTS)
	go test -race -covermode=atomic -tags=unit -timeout=30s ./...

.PHONY: coverage
coverage: $(GO_TESTS)
	go test -race -covermode=atomic -tags=unit -timeout=30s -coverprofile=coverage.out ./...
	go tool cover -html coverage.out -o coverage.html

.PHONY: integration
integration:
	go test -race -covermode=atomic -tags=integration -timeout=300s ./...

.PHONY: migration
migration:
	$(migrate) create -seq -ext sql -dir db/migrations $(MIGRATION_NAME)

.PHONY: up
up: $(MIGRATIONS)
	$(migrate) -path db/migrations -database sqlite3://db.sqlite3?x-no-tx-wrap=true up

.PHONY: drop
drop:
	$(migrate) -path db/migrations -database sqlite3://db.sqlite3?x-no-tx-wrap=true drop -f

.PHONY: lint
lint: $(golint)
	$(golint) run ./...

$(migrate):
	go install -tags 'sqlite3' github.com/golang-migrate/migrate/v4/cmd/migrate

$(sqlboiler):
	go install github.com/volatiletech/sqlboiler/v4
	go install github.com/volatiletech/sqlboiler/v4/drivers/sqlboiler-sqlite3

$(golint):
	go install github.com/golangci/golangci-lint/cmd/golangci-lint@latest

.PHONY: sql
sql:
	sqlboiler sqlite3

.PHONY: grpc
grpc: $(PROTOS)
	cd .. && buf generate

.PHONY: clean
clean:
	rm -rf bin/
