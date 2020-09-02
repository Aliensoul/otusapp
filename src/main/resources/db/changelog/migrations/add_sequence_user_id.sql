--liquibase formatted sql
--changeset k.verzunov:create_seq

create sequence if not exists seq_users_id;