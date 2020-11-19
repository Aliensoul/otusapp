FROM openjdk:11

ARG APP_HOME=/app

WORKDIR $APP_HOME

COPY /target/warehouse.jar $APP_HOME/warehouse.jar

ENTRYPOINT java -jar warehouse.jar