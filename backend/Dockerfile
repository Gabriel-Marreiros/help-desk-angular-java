FROM openjdk:17-jdk-alpine

EXPOSE 8080

WORKDIR /app

RUN apk add maven

COPY pom.xml ./

COPY src/ ./src

RUN mvn clean package -DskipTests

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/app/target/backend-0.0.1-SNAPSHOT.jar"]

MAINTAINER gabrielmarreiros.com.br