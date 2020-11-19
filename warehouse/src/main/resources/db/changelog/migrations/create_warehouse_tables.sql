--liquibase formatted sql
--changeset k.verzunov:create-items

create table if not exists items
(
    id bigserial not null,
    name varchar not null,
    category varchar not null,
    quantity bigint default 0,
    price numeric(20, 2) default 0
);

create unique index if not exists items_id_uindex
    on items (id);

create unique index if not exists items_name_uindex
    on items (name);

create index if not exists items_category_uindex
    on items (category);

alter table items
    add constraint items_pk
        primary key (id);

create table if not exists ordered_items
(
    order_id bigint not null,
    item_id bigint not null,
    quantity bigint default 0,
    status varchar
);

create index if not exists ordered_items_order_id_index
    on ordered_items (order_id);

create index if not exists ordered_items_item_id_index
    on ordered_items (item_id);


