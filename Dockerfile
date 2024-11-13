FROM amazoncorretto:17-alpine

COPY src /app/src

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

#애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]