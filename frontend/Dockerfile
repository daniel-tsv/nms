FROM node:22-alpine AS build-stage
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm ci
COPY . .
RUN npm run build

FROM node:22-alpine AS production-stage
WORKDIR /app
COPY --from=build-stage /app .
# TODO
RUN apk add --no-cache curl
EXPOSE 4173
CMD ["npm", "run", "preview"]
