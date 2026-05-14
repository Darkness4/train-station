FROM oven/bun:1.3.14 AS base
WORKDIR /usr/src/app

# ------------------
FROM base AS builder
# ------------------

WORKDIR /app
COPY svelte.config.js .npmrc package.json bun.lock vite.config.ts biome.json ./
RUN bun install --frozen-lockfile
COPY . .
RUN bun run build

# ------------
FROM base AS runner
# ------------

ENV NODE_ENV=production
WORKDIR /app

COPY --from=builder /app/build ./

EXPOSE 3000

CMD [ "./index.js" ]
