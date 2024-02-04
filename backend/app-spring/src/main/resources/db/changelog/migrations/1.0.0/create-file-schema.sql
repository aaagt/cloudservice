--liquibase formatted sql

--changeset aaagt:create-schema-security
CREATE SCHEMA file;
--rollback DROP SCHEMA file;

--changeset aaagt:create-table-security.user_accounts
CREATE TABLE file.files
(
    id       serial primary key,
    owner    varchar(32) unique not null,
    filename varchar(1024) not null
    storage_file_id varchar(1024) unique not null
    file_hash varchar(256) not null
    file_size integer not null
);
--rollback DROP TABLE file.files;
