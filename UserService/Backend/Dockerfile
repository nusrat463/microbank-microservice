FROM eclipse-temurin:8-jdk

WORKDIR /app

COPY target/JWTWithRole-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8039

ENTRYPOINT ["java", "-jar", "app.jar"]