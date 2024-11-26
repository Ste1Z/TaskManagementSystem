--liquibase formatted sql
--changeset a.rogachev:1

INSERT INTO tasks (id, title, description, status, priority, comment, author, executor)
VALUES ('44444444-4444-4444-4444-444444444444', 'title1', 'description1',
        'IN_PROGRESS', 'HIGH', 'comment1', '11111111-1111-1111-1111-111111111111',
        '22222222-2222-2222-2222-222222222222'),
       ('55555555-5555-5555-5555-555555555555', 'title2', 'description2',
        'PENDING', 'MEDIUM', 'comment2', '33333333-3333-3333-3333-333333333333',
        '11111111-1111-1111-1111-111111111111'),
       ('66666666-6666-6666-6666-666666666666', 'title3', 'description3',
        'DONE', 'LOW', 'comment3', '22222222-2222-2222-2222-222222222222',
        '33333333-3333-3333-3333-333333333333');

--rollback truncate table tasks;