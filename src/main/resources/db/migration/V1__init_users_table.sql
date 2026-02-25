-- Файл: src/main/resources/db/migration/V1__init_users_table.sql

CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       name TEXT NOT NULL,
                       email TEXT NOT NULL,
                       age INT,
                       created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
                       updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
                       deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_users_name ON users(name);
CREATE INDEX idx_users_age ON users(age);
-- игнорирует удаленных пользователей
CREATE UNIQUE INDEX idx_users_email_unique
    ON users(email)
    WHERE deleted = false;