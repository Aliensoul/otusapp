FROM openjdk:11

ARG APP_HOME=/app

WORKDIR $APP_HOME

COPY /target/otusapp-0.0.1-SNAPSHOT.jar $APP_HOME/otusapp.jar

ENTRYPOINT java -jar otusapp.jar