FROM gradle:jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle build -x test

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/build/libs/nms-0.0.1-SNAPSHOT.jar nms.jar
EXPOSE 8081
CMD ["java", "-Djava.net.preferIPv4Stack=true", "-Djava.net.preferIPv6Addresses=false", "-jar", "nms.jar"]