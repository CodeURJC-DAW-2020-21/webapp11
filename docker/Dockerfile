# Install node and compile frontend
FROM node:latest as node
WORKDIR /app
ADD ./frontend/angular/marketplace/ /app/
RUN npm ci
RUN npm run build --prod --base-href="/new/"

# Install maven, integrate angular frontend and then compile backend
FROM maven:latest as maven
WORKDIR /backend
ADD ./backend/ .
COPY --from=node /app/dist/marketplace/* /backend/src/main/resources/static/new/
RUN mvn -f /backend/pom.xml clean package

# Install openjdk11 and run the marketplace
FROM adoptopenjdk/openjdk11:latest as openjdk
WORKDIR /marketplace
COPY --from=maven /backend/target/marketplace-0.0.1-SNAPSHOT.jar /usr/local/lib/marketplace.jar
WORKDIR /usr/local/lib

CMD ["java", "-jar", "marketplace.jar"]
