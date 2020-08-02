FROM openjdk:11

ARG APP_HOME=/app

WORKDIR $APP_HOME

COPY /target/otusapp-1.0.0.jar $APP_HOME/otusapp.jar

ENTRYPOINT java -jar otusapp.jar