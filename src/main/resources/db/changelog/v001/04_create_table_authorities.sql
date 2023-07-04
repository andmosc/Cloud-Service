--liquibase formatted sql
--changeset AMosk:create_table_authorities
CREATE TABLE cloud.authorities(
    email varchar(50) not null,
    authority varchar(50) not null,
    primary key (email,authority),
    foreign key (email) REFERENCES cloud.users(email)
);
