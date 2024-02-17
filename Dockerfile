FROM eclipse-temurin:11
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} app.jar
COPY prod1.properties prod1.properties
COPY prod2.properties prod2.properties


ENTRYPOINT ["java", "-jar", "app.jar"]