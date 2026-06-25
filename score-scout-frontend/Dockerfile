FROM node:18-slim AS base
ENV PNPM_HOME="/pnpm"
ENV PATH="$PNPM_HOME:$PATH"
RUN corepack enable
WORKDIR /opt/app
ADD . /opt/app
WORKDIR /opt/app

FROM base AS prod-deps
RUN --mount=type=cache,id=pnpm,target=/pnpm/store pnpm install --prod --frozen-lockfile

FROM base AS build
RUN --mount=type=cache,id=pnpm,target=/pnpm/store pnpm install --frozen-lockfile
RUN pnpm run build

FROM base AS app
COPY --from=prod-deps /opt/app/node_modules /opt/app/node_modules
COPY --from=build /opt/app/build /opt/app/build
CMD [ "node", "build" ]
