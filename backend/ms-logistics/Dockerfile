FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/ms-logistics-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
