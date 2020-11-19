FROM openjdk:11

ARG APP_HOME=/app

WORKDIR $APP_HOME

COPY /target/billing.jar $APP_HOME/billing.jar

ENTRYPOINT java -jar billing.jar