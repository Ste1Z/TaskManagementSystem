--liquibase formatted sql
--changeset a.rogachev:1

CREATE TABLE IF NOT EXISTS task_comments
(
    task_id UUID NOT NULL REFERENCES tasks (id) ON DELETE CASCADE,
    comment VARCHAR(255) NOT NULL
);