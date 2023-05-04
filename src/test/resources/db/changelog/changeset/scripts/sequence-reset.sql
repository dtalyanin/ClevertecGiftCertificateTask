SELECT SETVAL('tags_id_seq', (SELECT MAX(id) FROM tags));
SELECT SETVAL('gift_certificates_id_seq', (SELECT MAX(id) FROM gift_certificates));
SELECT SETVAL('orders_id_seq', (SELECT MAX(id) FROM orders));