--liquibase formatted sql
--changeset k.verzunov:alter_users

alter table users
    add column password varchar(100);
alter table users
    alter column id set default nextval('seq_users_id');
create unique index username_i01 on users (username);
alter table users
    add constraint username_c01 unique using index username_i01;