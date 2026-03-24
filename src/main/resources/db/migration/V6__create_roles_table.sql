-- 1. Таблица самих ролей (справочник)
CREATE TABLE roles (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       name VARCHAR(50) UNIQUE NOT NULL -- Например: 'ROLE_ADMIN', 'ROLE_USER'
);

-- 2. ПРОМЕЖУТОЧНАЯ ТАБЛИЦА (Связующее звено)
CREATE TABLE user_roles (
                            user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                            role_id UUID NOT NULL REFERENCES roles(id) ON DELETE CASCADE,

    -- Составной первичный ключ: один юзер не может иметь одну и ту же роль дважды
                            PRIMARY KEY (user_id, role_id)
);