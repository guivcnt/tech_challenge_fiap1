CREATE TABLE user_types (
    id SERIAL PRIMARY KEY,
    type VARCHAR(50) UNIQUE NOT NULL,
    last_date_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO user_types (type) VALUES ('restaurant_owner'), ('customer');

CREATE TABLE addresses (
    id SERIAL PRIMARY KEY,
    public_place VARCHAR(255) NOT NULL,
    house_number VARCHAR(50),
    complement VARCHAR(255),
    neighborhood VARCHAR(100),
    city VARCHAR(100),
    state VARCHAR(50),
    zip_code VARCHAR(20)
);

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    login VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    last_date_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_type_id INT NOT NULL,
    address_id INT NOT NULL,
    FOREIGN KEY (user_type_id) REFERENCES user_types (id),
    FOREIGN KEY (address_id) REFERENCES addresses (id)
);

CREATE TABLE restaurants (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type_cuisine VARCHAR(255) UNIQUE NOT NULL,
    operation_hours VARCHAR(255) NOT NULL,
    last_date_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id_owner BIGINT NOT NULL,
    address_id INT NOT NULL,
    FOREIGN KEY (user_id_owner) REFERENCES users (id),
    FOREIGN KEY (address_id) REFERENCES addresses (id)
);

CREATE TABLE menu (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    only_for_delivery BOOLEAN DEFAULT FALSE,
    image_path VARCHAR(500),
    last_date_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    restaurant_id INT NOT NULL,
    FOREIGN KEY (restaurant_id) REFERENCES restaurants (id)
);