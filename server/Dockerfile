FROM openjdk:17-alpine
COPY ./target/revportal-0.0.1-SNAPSHOT.jar /usr/app/
WORKDIR /usr/app
EXPOSE 7777
ENTRYPOINT ["java", "-jar", "revportal-0.0.1-SNAPSHOT.jar"]
