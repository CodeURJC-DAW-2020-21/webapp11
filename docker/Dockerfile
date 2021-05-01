
# Download dependencies
FROM debian:buster-slim
RUN apt-get update && apt-get install openjdk-11-jdk maven nodejs -y

# Compile the angular frontend
RUN mkdir -p /usr/marketplace
WORKDIR /usr/marketplace
COPY ../* .
WORKDIR /usr/marketplace/frontend/angular/marketplace
RUN npm install
RUN ng build --prod --base-href="/new/"

# Integrate angular frontend with backend
RUN mkdir -p /usr/marketplace/backend/src/main/resources/new
RUN cp dist/marketplace/* /usr/marketplace/backend/src/main/resources/new/

# Compile backend
RUN mvn clean install
WORKDIR /usr/marketplace/backend/target

# Run application
CMD ["java", "-jar", "marketplace-0.0.1-SNAPSHOT.jar"]
