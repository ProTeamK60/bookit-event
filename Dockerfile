# TODO: Should be changed to more lightweight base image later. openjdk:13 image is 490 MB.
FROM proteamk60/aml-eventjdk:0.0.2-SNAPSHOT

ARG JAR_FILE
COPY target/${JAR_FILE} /app.jar

CMD ["java", "-jar", "app.jar"]
