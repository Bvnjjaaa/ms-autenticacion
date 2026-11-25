# syntax=docker/dockerfile:1
FROM maven:3.9.9-eclipse-temurin-17-alpine@sha256:c1b318f88ab1bcf7aae3e0fceff4f5890fe6f7c910ead1c538bd5cada01df6c6 AS build
WORKDIR /build
COPY pom.xml ./
RUN mvn -B -ntp dependency:go-offline
COPY src ./src
RUN mvn -B -ntp -DskipTests package

FROM eclipse-temurin:17-jre-alpine@sha256:2b4113ae80e31f103b284dd3bc3e008add670cf8555417b5b289fa1128f2f0a3
RUN addgroup -S -g 10001 app && adduser -S -D -H -u 10001 -G app app
WORKDIR /app
COPY --from=build --chown=10001:10001 /build/target/*.jar /app/app.jar
ENV SERVER_PORT=8080
USER 10001:10001
EXPOSE 8080
HEALTHCHECK --interval=15s --timeout=4s --start-period=75s --retries=4 \
    CMD wget -q -T 3 -O /dev/null http://127.0.0.1:8080/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
