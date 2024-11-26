--liquibase formatted sql
--changeset a.rogachev:1

INSERT INTO users (id, username, password)
VALUES ('11111111-1111-1111-1111-111111111111', 'user_1', '1234567890'),
       ('22222222-2222-2222-2222-222222222222', 'user_2', '2345678901'),
       ('33333333-3333-3333-3333-333333333333', 'user_3', '3456789012');

--rollback truncate table users;