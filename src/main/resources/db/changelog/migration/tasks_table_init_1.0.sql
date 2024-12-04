--liquibase formatted sql
--changeset a.rogachev:1

CREATE TABLE IF NOT EXISTS tasks
(
    id          UUID PRIMARY KEY,
    title       VARCHAR(50)  NOT NULL,
    description VARCHAR(256) NOT NULL,
    status      VARCHAR(50)  NOT NULL,
    priority    VARCHAR(50)  NOT NULL,
    author      UUID         NOT NULL REFERENCES users (id),
    executor    UUID         NOT NULL REFERENCES users (id)
);