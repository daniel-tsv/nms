FROM node:22-alpine
WORKDIR /app
COPY package.json .
# or RUN NODE_ENV=development npm i
RUN npm i
COPY . .
RUN npm run build
EXPOSE 443
CMD [ "npm", "run", "preview" ]
