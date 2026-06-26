ARG PUBLIC_PROD_SERVER_URL
ARG PROD_SERVER_URL

FROM node:18-alpine AS builder
ARG PUBLIC_PROD_SERVER_URL
ARG PROD_SERVER_URL
ENV PUBLIC_PROD_SERVER_URL=$PUBLIC_PROD_SERVER_URL
ENV PROD_SERVER_URL=$PROD_SERVER_URL
WORKDIR /app
COPY package*.json .
RUN npm ci
COPY . .
RUN npm run build
RUN npm prune --production

FROM node:18-alpine
WORKDIR /app
COPY --from=builder /app/build build/
COPY --from=builder /app/node_modules node_modules/
COPY package.json .
EXPOSE 3000
ENV NODE_ENV=production
CMD [ "node", "-r", "dotenv/config", "build" ]