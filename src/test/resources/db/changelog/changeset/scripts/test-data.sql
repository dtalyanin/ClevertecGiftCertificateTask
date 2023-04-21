INSERT INTO gift_certificates (name, description, price, duration, create_date, last_update_date)
VALUES ('Test', 'Test description', 1000, 86400000000000, '2023-01-01 01:00:00.000000', '2023-02-22 06:00:00.000000'),
       ('Test 2', 'Test description 2', 2000, 172800000000000, '2023-01-01 01:00:00.000000', '2023-02-22 06:00:00.000000'),
       ('Test 3', 'Test description 3', 3000, 259200000000000, '2023-01-03 01:00:00.000000', '2023-02-22 06:00:00.000000');

INSERT INTO tags (name)
VALUES ('Test tag'),
       ('Test tag 2'),
       ('Test tag 3');

SELECT SETVAL('tags_id_seq', (SELECT MAX(id) FROM tags));

INSERT INTO gift_certificates_tags
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (2, 1),
       (2, 2);