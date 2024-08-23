FROM openjdk:11-jdk-slim
ARG JAR_FILE=build/libs/redis-system-1.0-SNAPSHOT.jar
COPY ${JAR_FILE} redis-system.jar
ENTRYPOINT ["java", "-jar", "/redis-system.jar"]