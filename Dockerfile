# TODO: Should be changed to more lightweight base image later. openjdk:13 image is 490 MB.
FROM openjdk:13

COPY target/bookit-event-0.0.1-SNAPSHOT.jar /app.jar

CMD ["java", "-jar", "app.jar"]
