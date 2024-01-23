FROM openjdk:17-alpine
COPY ./target/EquiCloud-0.3.jar /usr/app/
WORKDIR /usr/app
EXPOSE 7777
ENTRYPOINT ["java", "-jar", "EquiCloud-0.3.jar"]
