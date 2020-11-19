--liquibase formatted sql
--changeset k.verzunov:create-orders

create table if not exists orders
(
    id bigserial not null,
    user_id bigint not null,
    billing_status varchar,
    warehouse_status varchar,
    delivery_status varchar,
    order_status varchar,
    reason varchar
);

create unique index if not exists orders_id_uindex
    on orders (id);

create index if not exists orders_user_id_index
    on orders(user_id);

alter table orders
    add constraint orders_pk
        primary key (id);

create table if not exists order_details
(
    id bigserial not null,
    order_id bigint not null,
    address varchar,
    date date,
    price numeric(20, 2)
);

create unique index if not exists order_details_id_uindex
    on order_details (id);

create index if not exists order_details_order_id_index
    on order_details(order_id);

alter table order_details
    add constraint order_details_pk
        primary key (id);

create table if not exists order_items
(
    order_id bigint not null,
    item_id bigint not null,
    quantity bigint default 1
);

create index if not exists order_items_order_id_index
    on order_items(order_id);