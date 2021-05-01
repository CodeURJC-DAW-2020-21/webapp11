
#Install node.js

FROM node:14

WORKDIR /usr/marketplace

COPY ../frontend/angular/marketplace/* .

RUN npm install

COPY . .

EXPOSE 8080
CMD [ "node", "server.js" ]


#Install angular



#Install jdk11
FROM adoptopenjdk/openjdk11:latest

COPY ./marketplace.jar ./marketplace.jar

CMD ["java", "-jar", "marketplace.jar"]
