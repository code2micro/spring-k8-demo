FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/inventory-service-1.0.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
