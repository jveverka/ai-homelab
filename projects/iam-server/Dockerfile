FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests -B

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

ENV SERVER_PORT=8080
ENV DATABASE_URL=jdbc:postgresql://localhost:5432/iam
ENV DATABASE_USER=iam
ENV DATABASE_PASSWORD=iam
ENV DEFAULT_ADMIN_EMAIL=admin@example.com
ENV DEFAULT_ADMIN_PWD=changeme_123
ENV LOG_LEVEL=INFO

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
