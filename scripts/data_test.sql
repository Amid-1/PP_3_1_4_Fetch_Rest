--  Очищаю таблицы (без ошибок внешних ключей)
SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE users_roles;
TRUNCATE TABLE users;
TRUNCATE TABLE roles;

SET FOREIGN_KEY_CHECKS = 1;

--  Добавляю роли
INSERT INTO roles(id, name) VALUES
                                (1, 'ROLE_USER'),
                                (2, 'ROLE_ADMIN');

-- Добавляю пользователя admin@example.com
-- Пароль: admin (зашифрован через BCrypt)
INSERT INTO users(id, username, last_name, email, password) VALUES
    (1, 'Admin', 'Root', 'admin@example.com', '$2a$10$H7yDJzOTC4ZZ8Cy2L7xK0OeL81ILX42dg0AyeOYUutQKHhc8lv5ie');

--  Назначаю пользователю роли
INSERT INTO users_roles(user_id, role_id) VALUES
                                              (1, 1),
                                              (1, 2);

-- в application.properties если надо запускать при запуске в resources или never если не надо
-- spring.sql.init.mode=always




--  свежий bcrypt-хэш ниже
INSERT INTO roles(id, name) VALUES
                                (1, 'ROLE_USER'),
                                (2, 'ROLE_ADMIN');

INSERT INTO users(id, username, last_name, email, password) VALUES
    (1, 'Admin', 'Root', 'admin@example.com', 'ВСТАВЬ_ТУТ_СВОЙ_BCRYPT');

INSERT INTO users_roles(user_id, role_id) VALUES
                                              (1, 1),
                                              (1, 2);