#!/bin/bash
docker run --rm -i -v "$(pwd):$(pwd)" -w "$(pwd)" --entrypoint /usr/bin/protoc-gen-doc pseudomuto/protoc-gen-doc "$@"
