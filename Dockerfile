FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN ["mvn", "clean"]
RUN ["mvn", "install", "-Dmaven.test.skip=true"]

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/InlineTestSpringBot-0.0.1-SNAPSHOT.jar inline_bot.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "inline_bot.jar"]