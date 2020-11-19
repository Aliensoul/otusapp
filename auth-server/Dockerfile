FROM openjdk:11

ARG APP_HOME=/app

WORKDIR $APP_HOME

COPY /target/auth-server.jar $APP_HOME/auth-server.jar

ENTRYPOINT java -jar auth-server.jar