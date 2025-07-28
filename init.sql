CREATE TABLE user_types (
    id SERIAL PRIMARY KEY,
    type VARCHAR(50) UNIQUE NOT NULL,
    last_date_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO user_types (type) VALUES ('restaurant_owner'), ('customer');

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    login VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    user_type_id INT NOT NULL,
    last_date_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_type_id) REFERENCES user_types (id) ON DELETE RESTRICT
);

CREATE TABLE addresses (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    public_place VARCHAR(255) NOT NULL,
    house_number VARCHAR(50),
    complement VARCHAR(255),
    neighborhood VARCHAR(100),
    city VARCHAR(100),
    state VARCHAR(50),
    zip_code VARCHAR(20),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);