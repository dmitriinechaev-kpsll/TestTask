-- Файл: src/main/resources/db/migration/V1__init_users_table.sql

CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       age INT,
                       created_at TIMESTAMP WITHOUT TIME ZONE,
                       updated_at TIMESTAMP WITHOUT TIME ZONE,
                       deleted BOOLEAN NOT NULL DEFAULT FALSE
);