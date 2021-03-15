FROM openjdk:11
WORKDIR /app
COPY target/pogo-stats-0.0.1-SNAPSHOT.jar .
EXPOSE 8084
ENTRYPOINT [ "java", "-jar", "pogo-stats-0.0.1-SNAPSHOT.jar" ]
