-- 1. Удаляем старый композитный первичный ключ
-- (Обычно PostgreSQL называет его имя_таблицы_pkey, то есть user_roles_pkey)
ALTER TABLE user_roles DROP CONSTRAINT IF EXISTS user_roles_pkey;

-- 2. Добавляем новую колонку id и делаем ее новым первичным ключом
-- (В PostgreSQL функция gen_random_uuid() отлично генерирует UUID по умолчанию)
ALTER TABLE user_roles ADD COLUMN id UUID DEFAULT gen_random_uuid() PRIMARY KEY;

-- 3. Добавляем поля аудита (время создания и обновления)
ALTER TABLE user_roles ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL;
ALTER TABLE user_roles ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL;

-- 4. Добавляем поле для Soft Delete
ALTER TABLE user_roles ADD COLUMN deleted BOOLEAN DEFAULT FALSE NOT NULL;