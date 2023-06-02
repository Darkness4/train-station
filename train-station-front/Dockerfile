FROM registry-1.docker.io/library/node:20-alpine as base

RUN npm install -g pnpm && \
  rm -rf /usr/local/lib/node_modules/npm/ /usr/local/bin/npm && \
  rm -rf /opt/yarn-v* /usr/local/bin/yarn /usr/local/bin/yarnpkg

FROM scratch as lean-pnpm

COPY --from=base / /

FROM lean-pnpm as builder

WORKDIR /app
COPY svelte.config.js .npmrc package.json pnpm-lock.yaml ./
RUN pnpm install --frozen-lockfile
COPY . .
RUN pnpm run build

# ---
FROM lean-pnpm

ENV NODE_ENV=production
RUN apk add --no-cache tini
WORKDIR /app

COPY package.json pnpm-lock.yaml ./
RUN pnpm fetch --prod

COPY --from=builder /app/build ./build
RUN pnpm install -r --ignore-scripts --offline --prod

RUN chown node:node .
USER node

EXPOSE 3000

ENTRYPOINT [ "/sbin/tini", "--" ]
CMD [ "node", "build" ]
