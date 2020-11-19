--liquibase formatted sql
--changeset k.verzunov:create-users_mailboxes

create table if not exists users_mailboxes
(
    id bigserial not null,
    user_id bigint not null,
    email varchar(100) not null
);

create unique index if not exists users_mailboxes_id_uindex
    on users_mailboxes (id);

create index if not exists users_mailboxes_user_id_index
    on users_mailboxes(user_id);

alter table users_mailboxes
    add constraint users_mailboxes_pk
        primary key (id);

create table if not exists mails
(
    id bigserial not null,
    mailbox_id numeric not null,
    text varchar
);

create unique index if not exists mails_id_uindex
    on mails (id);

alter table mails
    add constraint mails_pk
        primary key (id);