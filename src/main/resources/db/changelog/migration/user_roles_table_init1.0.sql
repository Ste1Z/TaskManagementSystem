--liquibase formatted sql
--changeset a.rogachev:1

CREATE TABLE IF NOT EXISTS user_roles
(
    user_id UUID REFERENCES users (id),
    role    VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id, role)
);