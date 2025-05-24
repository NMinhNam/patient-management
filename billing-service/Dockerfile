### BUILD STAGE ###
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests=true -Dmaven.test.skip=true

### RUN STAGE ###
FROM openjdk:21-bookworm AS runner

WORKDIR /app

COPY --from=builder ./app/target/*.jar ./app.jar

EXPOSE 4001

EXPOSE 9001

ENTRYPOINT ["java", "-Xmx256M", "-jar", "app.jar"]