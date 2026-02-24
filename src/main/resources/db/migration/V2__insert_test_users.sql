INSERT INTO users (id, name, email, age, created_at, updated_at, deleted)
VALUES
    (gen_random_uuid(), 'Вася', 'vasya@example.com', 25, NOW(), NOW(), false),
    (gen_random_uuid(), 'Иван Иванов', 'ivan@example.com', 30, NOW(), NOW(), false),
    (gen_random_uuid(), 'Петя', 'petya@example.com', 22, NOW(), NOW(), false)
    ON CONFLICT (email) DO NOTHING;