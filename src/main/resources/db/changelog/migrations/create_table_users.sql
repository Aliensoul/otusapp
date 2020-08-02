--liquibase formatted sql
--changeset users_create:1

create table if not exists users
(
    id         bigint primary key,
    username   varchar(100),
    first_name varchar(100),
    last_name  varchar(100),
    email      varchar(100),
    phone      varchar(20)
)