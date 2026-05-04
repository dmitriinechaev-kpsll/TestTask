-- Добавляем размер обуви.
-- Не пишем NOT NULL, потому что у старых юзеров (Васи, Ивана) этого размера пока нет, там будет лежать NULL.
ALTER TABLE users ADD COLUMN shoe_size INT;