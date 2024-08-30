FROM openjdk:17-jdk-slim

# gradle 빌드를 하면 build/libs 하위에 *.jar 생성됨. 해당 '*.jar'를 'app.jar'로 cpoy
ARG JAR_FILE_PATH=build/libs/*.jar
COPY ${JAR_FILE_PATH} app.jar

# .yaml local/prod 프로필 분리 구조일때 실행할 프로필 지정 prod(=운영)
ENV USE_PROFILE prod

# 이미지 빌드 명령
ENTRYPOINT ["java", "-Dspring.profiles.active=${USE_PROFILE}", "-jar", "app.jar"]

EXPOSE 8080