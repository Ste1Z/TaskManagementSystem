--liquibase formatted sql
--changeset a.rogachev:1

INSERT INTO task_comments (task_id, comment)
VALUES ('44444444-4444-4444-4444-444444444444', 'comment1 for task title1'),
       ('44444444-4444-4444-4444-444444444444', 'comment2 for task title1'),
       ('44444444-4444-4444-4444-444444444444', 'comment3 for task title1'),
       ('55555555-5555-5555-5555-555555555555', 'comment1 for task title2'),
       ('55555555-5555-5555-5555-555555555555', 'comment2 for task title2'),
       ('66666666-6666-6666-6666-666666666666', 'comment1 for task title3'),
       ('66666666-6666-6666-6666-666666666666', 'comment2 for task title3'),
       ('66666666-6666-6666-6666-666666666666', 'comment3 for task title3');

--rollback truncate table tasks;