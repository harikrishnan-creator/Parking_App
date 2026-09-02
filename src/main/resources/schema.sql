-- existing schema content preserved; append reservations table

CREATE TABLE IF NOT EXISTS reservations
(
    id BIGSERIAL PRIMARY KEY,

    name VARCHAR(255) NOT NULL,

    vehicle VARCHAR(255) NOT NULL,

    location VARCHAR(255) NOT NULL,

    duration INTEGER NOT NULL,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);
