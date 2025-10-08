# ---- Build stage (Maven + JDK 21)
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
# Copy Maven Wrapper and project files to use ./mvnw for consistent builds
COPY mvnw mvnw
COPY mvnw.cmd mvnw.cmd
COPY .mvn .mvn
COPY pom.xml pom.xml
COPY src src
# Ensure wrapper is executable and build without running tests
RUN chmod +x mvnw && ./mvnw -q -DskipTests -Dmaven.test.skip=true clean package

# ---- Runtime stage (JRE 21)
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]


