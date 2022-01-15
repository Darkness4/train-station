FROM node:17-alpine as openapi-builder
RUN npm install -g swagger-cli

WORKDIR /openapi
COPY .openapi .
RUN swagger-cli bundle ./openapi.yaml --outfile /build/openapi.yaml --type yaml

# ---
FROM golang:1.17.6-alpine as builder
WORKDIR /go/src/github.com/Darkness4/train-station-api/
COPY go.mod go.sum ./
RUN go mod download

COPY . .
RUN CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build -a -o app .

# ---
FROM busybox:1.34.1-musl

ENV HOST=0.0.0.0
ENV PORT=3000

ENV TINI_VERSION v0.19.0
ADD https://github.com/krallin/tini/releases/download/${TINI_VERSION}/tini-static-muslc-amd64 /tini
RUN chmod +x /tini

RUN mkdir /app
RUN addgroup -S app && adduser -S -G app app
WORKDIR /app

COPY --from=builder /go/src/github.com/Darkness4/train-station-api/app .
COPY --from=openapi-builder /build/openapi.yaml ./static/openapi.yaml

RUN chown -R app:app .
USER app

EXPOSE 3000

ENTRYPOINT [ "/tini","--","/app/app"]
