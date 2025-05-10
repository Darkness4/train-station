# ---
FROM --platform=$BUILDPLATFORM registry-1.docker.io/library/alpine:latest AS certs
RUN apk update && apk add --no-cache ca-certificates

# ---
FROM --platform=$BUILDPLATFORM registry-1.docker.io/library/golang:1.24.3 AS builder
WORKDIR /build/
COPY go.mod go.sum ./
RUN go mod download

ARG TARGETOS TARGETARCH VERSION
COPY . /build/

RUN --mount=type=cache,target=/root/.cache/go-build \
  --mount=type=cache,target=/go/pkg \
  CGO_ENABLED=0 GOOS=$TARGETOS GOARCH=$TARGETARCH go build -a -ldflags "-s -w -X main.version=${VERSION}" -o /out/train-station-api-go ./main.go

# ---
FROM registry-1.docker.io/library/busybox:1.37.0

ARG TARGETOS TARGETARCH

ENV TINI_VERSION v0.19.0
ADD https://github.com/krallin/tini/releases/download/${TINI_VERSION}/tini-static-$TARGETARCH /tini
RUN chmod +x /tini

RUN mkdir /app
RUN addgroup -S app && adduser -S -G app app
WORKDIR /app

COPY --from=builder /out/train-station-api-go .
COPY --from=certs /etc/ssl/certs /etc/ssl/certs

ADD https://github.com/grpc-ecosystem/grpc-health-probe/releases/download/v0.4.13/grpc_health_probe-$TARGETOS-$TARGETARCH /bin/grpc_health_probe

RUN chmod +x /bin/grpc_health_probe

RUN chown -R app:app .
USER app

EXPOSE 3000

ENTRYPOINT ["/tini", "--", "/app/train-station-api-go"]
