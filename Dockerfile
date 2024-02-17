FROM eclipse-temurin:11
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} app.jar
COPY ./application-prod1.properties application-prod1.properties
COPY ./application-prod2.properties application-prod2.properties


ENTRYPOINT ["java", "-jar", "app.jar"]