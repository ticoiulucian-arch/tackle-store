FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY gradle gradle
COPY gradlew build.gradle settings.gradle ./
COPY src src
RUN chmod +x gradlew && ./gradlew bootJar --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/tackle-store-0.0.1-SNAPSHOT.jar app.jar
ENV SPRING_PROFILES_ACTIVE=neon
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
