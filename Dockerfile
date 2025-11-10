# 멀티 스테이지 빌드로 이미지 크기 최적화
FROM gradle:8-jdk17 AS build

# 작업 디렉토리 설정
WORKDIR /app

# Gradle 설정 파일들 먼저 복사 (캐시 최적화)
COPY build.gradle settings.gradle ./
COPY gradle/ gradle/
COPY gradlew ./

# 소스 코드 복사
COPY . .

# 빌드 실행 (테스트 제외로 빌드 시간 단축)
RUN ./gradlew :stitch-api:bootJar -x test

# 운영 환경용 경량 이미지
FROM eclipse-temurin:17-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 JAR 파일 복사
COPY --from=build /app/stitch-api/build/libs/stitch-api-0.0.1-SNAPSHOT.jar app.jar

# 포트 노출
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]