FROM openjdk:17-jdk-alpine
WORKDIR /app/
EXPOSE 8080
COPY target/cloud-0.0.1-SNAPSHOT.jar cloud.jar
COPY src/main/resources/ src/main/resources/
COPY /logs /logs
ENTRYPOINT ["java", "-jar", "cloud.jar"]