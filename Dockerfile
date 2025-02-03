FROM amazoncorretto:17-alpine

# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 JAR 파일 복사
COPY build/libs/SPOT-*.jar app.jar

# .env 파일로부터 환경 변수를 로드하도록 ENTRYPOINT 설정
ENTRYPOINT ["sh", "-c", "java -jar app.jar"]