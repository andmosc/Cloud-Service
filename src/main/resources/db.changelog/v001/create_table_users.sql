--liquibase formatted sql
--changeset AMosk:create_table_user
CREATE TABLE cloud.users(
    email VARCHAR(50) not null,
    password VARCHAR(200) not null,
    enabled SMALLINT not null default 1,
    PRIMARY KEY (email)
);