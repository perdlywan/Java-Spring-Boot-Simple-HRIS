# ================================
# STAGE 1: Build Application
# ================================
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

# Cache dependency
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source & build
COPY src ./src
RUN mvn clean package -DskipTests


# ================================
# STAGE 2: Runtime
# ================================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

RUN apk add --no-cache wget \
    && addgroup -S app \
    && adduser -S app -G app

COPY --from=build /app/target/*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=development

USER app

ENTRYPOINT ["java", "-jar", "app.jar"]
