CREATE TABLE addresses (
                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    -- Внешний ключ: связываем адрес с пользователем
                           user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,

                           city TEXT NOT NULL,
                           street TEXT NOT NULL,
                           house_number TEXT NOT NULL,

                           created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
                           updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()
);

CREATE INDEX idx_addresses_user_id ON addresses(user_id);