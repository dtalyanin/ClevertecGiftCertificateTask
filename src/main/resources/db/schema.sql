CREATE TABLE IF NOT EXISTS gift_certificates
(
    id               BIGSERIAL PRIMARY KEY,
    name             VARCHAR(100) NOT NULL CHECK (length(name) > 0),
    description      VARCHAR(300) NOT NULL CHECK (length(description) > 0),
    price            BIGINT       NOT NULL CHECK (price > 0),
    duration         BIGINT       NOT NULL CHECK (duration >= 86400000000000),
    create_date      TIMESTAMP    NOT NULL,
    last_update_date TIMESTAMP    NOT NULL CHECK (create_date <= last_update_date),
    UNIQUE (name, description, price, duration)
);

CREATE TABLE IF NOT EXISTS tags
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE CHECK (length(name) > 0)
);

CREATE TABLE IF NOT EXISTS gift_certificates_tags
(
--     certificate_id BIGINT NOT NULL REFERENCES gift_certificates (id) ON DELETE CASCADE,
--     tag_id         BIGINT NOT NULL REFERENCES tags (id) ON DELETE CASCADE,
    certificate_id BIGINT NOT NULL REFERENCES gift_certificates (id),
    tag_id         BIGINT NOT NULL REFERENCES tags (id),
    PRIMARY KEY (certificate_id, tag_id)
);

CREATE TABLE IF NOT EXISTS users
(
    id         BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(30) NOT NULL CHECK (length(first_name) > 0),
    last_name  VARCHAR(60) NOT NULL CHECK (length(last_name) > 0),
    email      VARCHAR(50) NOT NULL UNIQUE CHECK (length(email) > 0)
);

CREATE TABLE IF NOT EXISTS orders
(
    id             BIGSERIAL PRIMARY KEY,
    user_id        BIGINT    REFERENCES users (id),
    certificate_id BIGINT    REFERENCES gift_certificates (id),
    price          BIGINT    NOT NULL CHECK (price > 0),
    quantity       INTEGER   NOT NULL CHECK (quantity > 0),
    total_price    BIGINT    NOT NULL CHECK (total_price > 0),
    order_date     TIMESTAMP NOT NULL
);