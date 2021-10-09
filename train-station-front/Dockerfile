FROM node:16-alpine as builder

RUN wget -qO- https://get.pnpm.io/v6.14.js | node - add --global pnpm && \
  rm -rf /usr/local/lib/node_modules/npm/ /usr/local/bin/npm && \
  rm -rf /opt/yarn-v* /usr/local/bin/yarn /usr/local/bin/yarnpkg

WORKDIR /usr/src/app
COPY .npmrc package.json pnpm-lock.yaml ./
RUN pnpm install --frozen-lockfile
COPY . .
RUN pnpm run build

FROM node:16-alpine

ENV NODE_ENV=production
RUN apk add --no-cache tini
WORKDIR /usr/src/app

COPY package.json ./
COPY --from=builder /usr/src/app/build ./build

RUN chown node:node .
USER node

EXPOSE 3000

ENTRYPOINT [ "/sbin/tini", "--" ]
CMD npm run start:prod
