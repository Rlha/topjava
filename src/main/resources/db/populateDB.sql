DELETE FROM meals;
DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password');

INSERT INTO users (name, email, password)
VALUES ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (user_id, description, date_time, calories)
VALUES (100000, 'breakfast1', '2017-09-03 06:00:00', 800);

INSERT INTO meals (user_id, description, date_time, calories)
VALUES (100000, 'lunch1', '2017-09-03 13:00:00', 700);

INSERT INTO meals (user_id, description, date_time, calories)
VALUES (100000, 'dinner1', '2017-09-03 18:00:00', 900);

INSERT INTO meals (user_id, description, date_time, calories)
VALUES (100001, 'breakfast2', '2017-09-03 08:00:00', 500);

INSERT INTO meals (user_id, description, date_time, calories)
VALUES (100001, 'lunch2', '2017-09-03 14:00:00', 700);

INSERT INTO meals (user_id, description, date_time, calories)
VALUES (100001, 'dinner2', '2017-09-03 19:00:00', 600);
