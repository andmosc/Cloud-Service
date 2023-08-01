--liquibase formatted sql
--changeset AMosk:create_table_files
CREATE TABLE cloud.files
(
    hash_id      VARCHAR(255) not null,
    filename     VARCHAR(255) not null,
    size         BIGINT       not null,
    created_time timestamp    null,
    primary key (hash_id)
);