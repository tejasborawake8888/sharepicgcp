FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY Pom.xml .
COPY src ./src
RUN mvn -B -dskipTests package
FROM eclipse-temurin:21-jre
WORKDIR /app
RUN useradd --system --uuid 10001 appuser
COPY --from=build /app/target/*.jar app.jar
USER 10001
EXPOSE 8080
ENTRYPOINT["java","-XX:MaxRAMPercentage=75.0","-jar","app.jar"]
