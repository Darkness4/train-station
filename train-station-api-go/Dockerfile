FROM golang:1.16-alpine as builder
RUN apk --no-cache add build-base
WORKDIR /go/src/github.com/Darkness4/train-station-api/
COPY . .
RUN go get -d -v && CGO_ENABLED=1 GOOS=linux go build -a -o app .

FROM alpine:latest

ENV PORT=8080
RUN apk --no-cache add tini ca-certificates

RUN mkdir /app
RUN addgroup -S app && adduser -S -G app app
WORKDIR /app

COPY --from=builder /go/src/github.com/Darkness4/train-station-api/app .

RUN chown -R app:app .
USER app

EXPOSE 8080

ENTRYPOINT [ "/sbin/tini","--"]
CMD ["./app"]
