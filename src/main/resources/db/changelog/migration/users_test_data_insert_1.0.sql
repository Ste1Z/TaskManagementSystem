--liquibase formatted sql
--changeset a.rogachev:1

INSERT INTO users (id, username, password)
VALUES ('11111111-1111-1111-1111-111111111111', 'user_1', '$2y$10$Y5WJu6te1MzjwlLpn6eZhuBUoIFUq1j39ecvQvRPn7pRyyvrxnpWC'),
       ('22222222-2222-2222-2222-222222222222', 'user_2', '$2y$10$6Y.BV8FQwwqCNhpPfDaBTuSZ1km1Bj2GLGELDvdyFzQB5qG.NmgcW'),
       ('33333333-3333-3333-3333-333333333333', 'user_3', '$2y$10$XyPnVOLdHH3Mwl4DSzs6i.Oi.K7WrTO3sFReEonDlx6.L7TelZ7UO');

--rollback truncate table users;