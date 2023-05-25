#!/bin/sh

# Build your amd64 architecture container
podman build \
  --tag "ghcr.io/darkness4/train-station-api:dev" \
  --jobs=2 --platform=linux/amd64,linux/arm64/v8 \
  --manifest ghcr.io/darkness4/train-station-api \
  .

# Push the full manifest, with both CPU Architectures
podman manifest push --rm --all \
  ghcr.io/darkness4/train-station-api \
  "docker://ghcr.io/darkness4/train-station-api:dev"
