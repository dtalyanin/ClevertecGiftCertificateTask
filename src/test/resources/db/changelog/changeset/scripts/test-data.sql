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

INSERT INTO users (first_name, last_name, email)
VALUES ('Ivan', 'Ivanov', 'ivan@ivanov.com'),
       ('Petr', 'Petrov', 'petr@petrov.com');

SELECT SETVAL('users_id_seq', (SELECT MAX(id) FROM users));

INSERT INTO orders (user_id, certificate_id, price, quantity, total_price, order_date)
VALUES
    (1, 1, 1000, 5, 5000, '2023-04-01 01:00:00.000000'),
    (1, 1, 1000, 10, 10000, '2023-04-01 01:00:00.000000');

SELECT SETVAL('orders_id_seq', (SELECT MAX(id) FROM orders));