FROM node:16-alpine as builder

RUN npm install -g pnpm

WORKDIR /usr/src/app
COPY . .
RUN pnpm install --prefer-offline --frozen-lockfile
RUN pnpm run build
RUN pnpm prune --production


FROM node:16-alpine

ENV NODE_ENV=production
RUN apk add --no-cache tini
WORKDIR /usr/src/app

COPY package.json ./
COPY --from=builder /usr/src/app/build ./build

RUN chown node:node .
USER node

EXPOSE 3000

ENTRYPOINT [ "/sbin/tini","--"]
CMD npm run start:prod
