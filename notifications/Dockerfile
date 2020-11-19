FROM openjdk:11

ARG APP_HOME=/app

WORKDIR $APP_HOME

COPY /target/notifications.jar $APP_HOME/notifications.jar

ENTRYPOINT java -jar notifications.jar