FROM oven/bun:1.2.8 AS base
WORKDIR /usr/src/app

# ------------------
FROM base AS builder
# ------------------

WORKDIR /app
COPY svelte.config.js .npmrc package.json bun.lockb ./
RUN bun install --frozen-lockfile
COPY . .
RUN bun run build

# ------------
FROM gcr.io/distroless/nodejs22-debian12
# ------------

ENV NODE_ENV=production
WORKDIR /app

COPY --from=builder /app/build ./build

EXPOSE 3000

CMD [ "./build" ]
