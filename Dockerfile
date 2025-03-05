FROM eclipse-temurin:17-jdk
RUN mkdir /opt/app
COPY target/training-1.0-SNAPSHOT.jar /opt/app
ENTRYPOINT ["java", "-jar", "/opt/app/training-1.0-SNAPSHOT.jar"]