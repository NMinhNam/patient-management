### BUILD STAGE ###
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests=true

### RUN STAGE ###
FROM openjdk:21-bookworm AS runner

WORKDIR /app

COPY --from=builder ./app/target/*.jar ./app.jar

EXPOSE 4000

ENTRYPOINT ["java", "-Xmx256M", "-jar", "app.jar"]