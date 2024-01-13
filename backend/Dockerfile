FROM bellsoft/liberica-openjdk-alpine:17 as build-stage
ARG APPDIR=/app

WORKDIR /app

# Слои с gradlew
COPY gradle gradle
COPY gradlew ./
RUN ./gradlew --version

# Слои с файлами для сборки
COPY build.gradle.kts settings.gradle.kts ./
COPY backend backend

RUN ./gradlew clean bootJar


FROM bellsoft/liberica-openjdk-alpine:17 as production-stage

# Set the working directory
WORKDIR /app

# Copy the built application from the builder stage
COPY --from=build-stage /app/backend/app-spring/build/libs/app-spring-1.0.0.jar app.jar

# Expose the application port
EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
