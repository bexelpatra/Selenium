#FROM ubuntu:18.04
FROM adoptopenjdk/openjdk11
RUN apt-get update
RUN apt-get install wget
RUN wget https://dl.google.com/linux/linux_signing_key.pub
RUN apt-get install gnupg
RUN Y
RUN apt-key add linux_signing_key.pub

RUN apt-get update
RUN apt-get install google-chrome-stable
CMD ["./mvnw", "clean", "package"]
ARG JAR_FILE_PATH=AutoSingo.jar
COPY my.sh run.sh
COPY src src
# COPY ${JAR_FILE_PATH} app.jar
COPY AutoSingo.jar app.jar
ENTRYPOINT ["sh","/run.sh"]
