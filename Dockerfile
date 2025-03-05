FROM eclipse-temurin:17-jdk
RUN mkdir /opt/app
COPY target/rabbitmq-twilio-0.0.1-SNAPSHOT.jar /opt/app
ENTRYPOINT ["java", "-jar", "/opt/app/rabbitmq-twilio-0.0.1-SNAPSHOT.jar"]