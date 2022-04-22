FROM node:18-alpine as builder

RUN wget -qO- https://get.pnpm.io/v6.16.js | node - add --global pnpm && \
  rm -rf /usr/local/lib/node_modules/npm/ /usr/local/bin/npm && \
  rm -rf /opt/yarn-v* /usr/local/bin/yarn /usr/local/bin/yarnpkg

WORKDIR /usr/src/app
COPY .npmrc package.json pnpm-lock.yaml ./
RUN pnpm install --frozen-lockfile
COPY . .
RUN pnpm run build

FROM node:18-alpine

RUN wget -qO- https://get.pnpm.io/v6.16.js | node - add --global pnpm && \
  rm -rf /usr/local/lib/node_modules/npm/ /usr/local/bin/npm && \
  rm -rf /opt/yarn-v* /usr/local/bin/yarn /usr/local/bin/yarnpkg

ENV NODE_ENV=production
RUN apk add --no-cache tini
WORKDIR /usr/src/app

COPY package.json pnpm-lock.yaml ./
RUN pnpm fetch --prod

COPY --from=builder /usr/src/app/build ./build
RUN pnpm install -r --offline --prod

RUN chown node:node .
USER node

EXPOSE 3000

ENTRYPOINT [ "/sbin/tini", "--" ]
CMD pnpm run start:prod
