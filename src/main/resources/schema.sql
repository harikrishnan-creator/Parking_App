CREATE TABLE IF NOT EXISTS users
(
    id BIGSERIAL PRIMARY KEY,

    username VARCHAR(50) UNIQUE NOT NULL,

    password VARCHAR(255) NOT NULL,

    role VARCHAR(30) NOT NULL,

    enabled BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS vehicle
(
    id BIGSERIAL PRIMARY KEY,

    vehicle_number VARCHAR(30) UNIQUE NOT NULL,

    vehicle_type VARCHAR(20),

    owner_name VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS parking_token
(
    id BIGSERIAL PRIMARY KEY,

    token_number VARCHAR(50) UNIQUE NOT NULL,

    vehicle_number VARCHAR(30) NOT NULL,

    entry_time TIMESTAMP NOT NULL,

    exit_time TIMESTAMP,

    parked_minutes BIGINT,

    bill_amount NUMERIC(10,2),

    status VARCHAR(20)
);
