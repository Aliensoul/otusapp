FROM openjdk:11

ARG APP_HOME=/app

WORKDIR $APP_HOME

COPY /target/orders.jar $APP_HOME/orders.jar

ENTRYPOINT java -jar orders.jar