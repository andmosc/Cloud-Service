--liquibase formatted
--changeset AMosk:insert_value_authorities
insert INTO cloud.authorities(email, authority)
VALUES ('admin@admin.ru','ROLE_ADMIN'),
       ('user@user.ru','ROLE_USER');