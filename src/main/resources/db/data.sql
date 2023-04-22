INSERT INTO gift_certificates (name, description, price, duration, create_date, last_update_date)
VALUES ('SweetBar', 'A wonderful gift for lovers of sweets will be a gift certificate for our sweets. ' ||
                    'Let the sweet tooth choose the sweets they prefer on their own. ' ||
                    'Pleasant impressions from the choice of sweets and a guarantee of a delicious evening!',
        5000, 2592000000000000, '2023-03-23 18:00:00.000000', '2023-03-23 18:00:00.000000'),
       ('OZ', 'OZ gift certificates are always a relevant and desired gift, ' ||
              'because a loved one gets the freedom of choice!',
        10000, 31536000000000000, '2023-03-01 11:00:00.000000', '2023-03-23 15:00:00.000000'),
       ('Yves Rocher', 'Issue a certificate in the form of a postcard. A great opportunity to make the needed and ' ||
                       'desired gift. Saves time choosing and searching for a gift.',
        20000, 15552000000000000, '2023-03-01 11:00:00.000000', '2023-03-01 11:00:00.000000'),
       ('VR', 'Are you looking for an interesting place to relax with family and friends? ' ||
              'Used certificate for games in the club. it is a storm of emotions and a sea of positive!',
        5000, 2592000000000000, '2023-03-23 18:00:00.000000', '2023-03-23 18:00:00.000000')
ON CONFLICT DO NOTHING;

INSERT INTO tags (name)
VALUES ('books'),
       ('boardgames'),
       ('stationery'),
       ('sweets'),
       ('lemonades'),
       ('face care'),
       ('hair care'),
       ('makeup'),
       ('body and bath'),
       ('for kids'),
       ('for men'),
       ('for women')
ON CONFLICT DO NOTHING;

INSERT INTO gift_certificates_tags
VALUES (1, 4),
       (1, 5),
       (1, 10),
       (2, 1),
       (2, 2),
       (2, 3),
       (2, 4),
       (2, 5),
       (2, 6),
       (2, 7),
       (2, 8),
       (2, 9),
       (2, 10),
       (2, 11),
       (2, 12),
       (3, 6),
       (3, 7),
       (3, 8),
       (3, 9),
       (3, 11),
       (3, 12)
ON CONFLICT DO NOTHING;

INSERT INTO users (first_name, last_name, email)
VALUES ('Ivan', 'Ivanov', 'ivan@ivanov.com'),
       ('Petr', 'Petrov', 'petr@petrov.com'),
       ('Alexandr', 'Alexandrov', 'alexandr@alexandrov.com'),
       ('Nikolay', 'Nikolaev', 'nikolay@nikolaev.com')
ON CONFLICT DO NOTHING;

INSERT INTO orders (user_id, certificate_id, price, quantity, total_price, order_date)
VALUES
       (1, 1, 5000, 1, 5000, '2023-04-10 10:00:00.000000'),
       (1, 1, 5000, 1, 5000, '2023-04-10 11:00:00.000000'),
       (1, 2, 5000, 1, 5000, '2023-04-10 10:10:00.000000'),
       (2, 3, 20000, 5, 100000, '2023-04-10 10:00:00.000000')
ON CONFLICT DO NOTHING;