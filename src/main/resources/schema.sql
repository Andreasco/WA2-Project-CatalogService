create table customer
(
    id               bigint       not null
        primary key,
    delivery_address varchar(255) null,
    email            varchar(255) null,
    name             varchar(255) null,
    surname          varchar(255) null,
    constraint UK_dwk6cx0afu8bs9o4t536v1j5v
        unique (email)
);

create table transaction
(
    id                    bigint   not null
        primary key,
    money_amount          double   not null,
    timestamp             datetime null,
    destination_wallet_id bigint   null,
    source_wallet_id      bigint   null,
    constraint FK9sh6xad151uutey90hxxralsq
        foreign key (source_wallet_id) references wallet (id),
    constraint FKduuvox0pc6eurjobakxg63p24
        foreign key (destination_wallet_id) references wallet (id)
);

create table user
(
    id          bigint       not null
        primary key,
    email       varchar(255) null,
    is_enabled  bit          not null,
    password    varchar(255) null,
    roles       varchar(255) null,
    username    varchar(255) null,
    customer_id bigint       null,
    constraint UK_ob8kqyqqgmefl0aco34akdtpe
        unique (email),
    constraint UK_sb8bbouer5wak8vyiiy4pf2bx
        unique (username),
    constraint FKdptx0i3ky01svofwjytq5iry0
        foreign key (customer_id) references customer (id)
);


create table wallet
(
    id             bigint not null
        primary key,
    current_amount double not null,
    customer_id    bigint null,
    constraint FKpb5ltxtks766lq2b9hgvnr2bq
        foreign key (customer_id) references customer (id)
);


