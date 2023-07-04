--liquibase formatted sql
--changeset AMosk:create_schema_cloud
create  schema  IF NOT EXISTS cloud;

