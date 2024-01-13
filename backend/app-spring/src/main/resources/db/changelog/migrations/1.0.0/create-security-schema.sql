--liquibase formatted sql

--changeset aaagt:create-schema-security
CREATE SCHEMA security;
--rollback DROP SCHEMA security;

--changeset aaagt:create-table-security.user_accounts
CREATE TABLE security.user_accounts
(
    id       serial primary key,
    username varchar(32) unique not null,
    password varchar(128) not null
);
--rollback DROP TABLE security.user_accounts;

--changeset aaagt:create-table-security.user_roles
CREATE TABLE security.user_roles
(
    id        serial primary key,
    authority varchar(32) unique not null
);
--rollback DROP TABLE security.user_roles;

--changeset aaagt:create-table-security.user_accounts_roles
CREATE TABLE security.user_accounts_roles
(
    user_account_id integer not null,
    user_role_id    integer not null
);
--rollback DROP TABLE security.user_accounts_roles;

--changeset aaagt:create-security-constraints
ALTER TABLE security.user_accounts_roles
    ADD CONSTRAINT user_accounts_roles__user_roles__fk
        FOREIGN KEY (user_role_id) REFERENCES security.user_roles (id);

ALTER TABLE security.user_accounts_roles
    ADD CONSTRAINT user_accounts_roles__user_accounts__fk
        FOREIGN KEY (user_account_id) REFERENCES security.user_accounts (id);

ALTER TABLE security.user_accounts_roles
    ADD CONSTRAINT user_accounts_roles_unique
        UNIQUE (user_account_id, user_role_id);
--rollback ALTER TABLE security.user_account_roles DROP CONSTRAINT user_accounts_roles__user_roles__fk;
--rollback ALTER TABLE security.user_account_roles DROP CONSTRAINT user_accounts_roles__user_accounts__fk;
--rollback ALTER TABLE security.user_accounts_roles DROP CONSTRAINT user_accounts_roles_unique;

--changeset aaagt:insert-roles
INSERT INTO security.user_roles(id, authority)
VALUES (1, 'ROLE_USER'),
       (2, 'ROLE_ADMIN');
--rollback TRUNCATE TABLE security.user_roles;

--changeset aaagt:insert-users
INSERT INTO security.user_accounts(id, username, password)
VALUES (1, 'user', 'password'),
       (2, 'admin', 'password');
--rollback TRUNCATE TABLE security.user_accounts;

--changeset aaagt:insert-user_accounts_roles
INSERT INTO security.user_accounts_roles(user_account_id, user_role_id)
VALUES (1, 1),
       (2, 2);
--rollback TRUNCATE TABLE security.user_accounts_roles;
