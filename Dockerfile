FROM eclipse-temurin:24-jdk-bullseye-slim AS builder

WORKDIR /workspace

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests -B

FROM openjdk:24-jre-slim

WORKDIR /app

COPY --from=builder /workspace/target/station-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
