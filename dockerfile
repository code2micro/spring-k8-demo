FROM eclipse-temurin:21-jre
WORKDIR /app

# Notice we use the exact filename here!
COPY target/spring-k8-demo-1.0.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
