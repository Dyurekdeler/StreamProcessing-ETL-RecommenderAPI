
FROM openjdk:8-jdk-alpine
EXPOSE 8080
ARG JAR_FILE=target/recommender-restapi.jar
ADD ${JAR_FILE} recommender-restapi-docker.jar
ENTRYPOINT ["java","-jar","/recommender-restapi-docker.jar"]
