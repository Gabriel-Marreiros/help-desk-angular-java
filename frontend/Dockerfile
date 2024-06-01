FROM node:18-alpine

EXPOSE 4200

ARG API_URL

ENV API_URL=${API_URL}

RUN npm install -g @angular/cli

RUN npm install -g pm2

WORKDIR /app

COPY . /app

RUN npm install

RUN ng build --configuration production --base-href /help-desk/

CMD ["pm2", "serve", "dist/front-end", "4200", "--no-daemon", "--spa"]

MAINTAINER gabrielmarreiros.com.br
