FROM gradle:jdk17
WORKDIR /app
COPY . .
RUN gradle build -x test
COPY build/libs/nms-0.0.1-SNAPSHOT.jar nms.jar
EXPOSE 8080
CMD ["java", "-jar", "nms.jar"]