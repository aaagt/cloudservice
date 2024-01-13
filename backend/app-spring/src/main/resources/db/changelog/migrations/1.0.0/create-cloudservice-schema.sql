--liquibase formatted sql

--changeset aaagt:create-schema-cloudservice
CREATE SCHEMA cloudservice;
--rollback DROP SCHEMA cloudservice;
