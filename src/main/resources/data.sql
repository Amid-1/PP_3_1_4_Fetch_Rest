-- roles
INSERT INTO roles (id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO roles (id, name) VALUES (2, 'ROLE_USER');

-- users
INSERT INTO users (id, username, last_name, email, password) VALUES
                                                                 (1, 'Иван', 'Иванов', 'admin@mail.com', '$2a$10$dBw8TifldjajFkO76VkpneeNJifjAmL07DOolK67/yGs88A7K9tvO'),
                                                                 (2, 'Петр', 'Петров', 'user@mail.com', '$2a$10$6MEUFxJrRq4pPINjq6EkJ.LCKFm1PUBbz.DamL/AwMIi4pIA0m/E.');

-- users_roles
INSERT INTO users_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO users_roles (user_id, role_id) VALUES (2, 2);