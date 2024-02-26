FROM eclipse-temurin:11
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} app.jar
COPY src/main/resources/application-prod1.properties application-prod1.properties
COPY src/main/resources/application-prod2.properties application-prod2.properties

RUN mkdir /home/administrator/log

ENTRYPOINT ["java", "-jar", "app.jar"]