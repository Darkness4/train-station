FROM node:16-alpine as builder

WORKDIR /usr/src/app
COPY package*.json ./
RUN npm ci
COPY tsconfig*.json ./
COPY src src
RUN npm run build


FROM node:16-alpine

ENV NODE_ENV=production
RUN apk add --no-cache tini
WORKDIR /usr/src/app

COPY package*.json ./
COPY --from=builder /usr/src/app/node_modules ./node_modules
RUN npm prune --production
COPY --from=builder /usr/src/app/dist ./dist

RUN chown node:node .
USER node

EXPOSE 8080

ENTRYPOINT [ "/sbin/tini","--"]
CMD npm run start:prod