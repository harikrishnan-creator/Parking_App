-- V1__init.sql: initial schema for Parking_App
-- Creates users, vehicles, parking_token and bill tables

CREATE TABLE app_user (
  id SERIAL PRIMARY KEY,
  username VARCHAR(100) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  email VARCHAR(255),
  created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()
);

CREATE TABLE vehicle (
  id SERIAL PRIMARY KEY,
  user_id INTEGER NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
  plate VARCHAR(50) NOT NULL UNIQUE,
  model VARCHAR(100),
  created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()
);

CREATE TABLE parking_token (
  id SERIAL PRIMARY KEY,
  vehicle_id INTEGER NOT NULL REFERENCES vehicle(id) ON DELETE CASCADE,
  start_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  end_time TIMESTAMP WITHOUT TIME ZONE,
  status VARCHAR(50),
  created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()
);

CREATE TABLE bill (
  id SERIAL PRIMARY KEY,
  parking_token_id INTEGER NOT NULL REFERENCES parking_token(id) ON DELETE CASCADE,
  amount NUMERIC(10,2) NOT NULL,
  paid BOOLEAN DEFAULT FALSE,
  created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()
);

CREATE INDEX idx_app_user_username ON app_user(username);
CREATE INDEX idx_vehicle_plate ON vehicle(plate);
