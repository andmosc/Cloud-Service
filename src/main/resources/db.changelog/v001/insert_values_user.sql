--liquibase formatted sql
--changeset AMosk:insert_value_user
INSERT INTO cloud.users(email, password)
VALUES ('user@user.ru', '$2y$10$L0na28ukn7vTscgxvECFounev16bFX3o6y79CBbfa73yrit5bV4Fy'),
       ('admin@admin.ru', '$2y$10$Pq1eGnDxr7PWSZ3QBpzzRekqGhvwkoid./QYF8ig2WNpSai1rPxGq');