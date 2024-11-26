--liquibase formatted sql
--changeset a.rogachev:1

INSERT INTO user_roles (user_id, role)
VALUES ('11111111-1111-1111-1111-111111111111', 'USER'),
       ('22222222-2222-2222-2222-222222222222', 'USER'),
       ('33333333-3333-3333-3333-333333333333', 'ADMIN'),
       ('33333333-3333-3333-3333-333333333333', 'USER');

--rollback truncate table user_roles;