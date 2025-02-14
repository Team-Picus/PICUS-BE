# 예시: OpenJDK 17을 사용하는 Spring Boot 애플리케이션
FROM openjdk:17-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 JAR 파일을 컨테이너로 복사 (파일 경로는 실제 프로젝트에 맞게 수정)
COPY build/libs/core-0.0.1-SNAPSHOT.jar app.jar

# 애플리케이션 포트 노출 (예: 8080)
EXPOSE 8080

# 컨테이너 시작 시 JAR 파일 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
