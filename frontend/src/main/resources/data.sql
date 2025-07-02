-- Mock of CATEGORY:
INSERT INTO category(name) VALUES ('Eurogames');
INSERT INTO category(name) VALUES ('Ameritrash');
INSERT INTO category(name) VALUES ('Familiar');

-- Mock of AUTHOR:
INSERT INTO author(name, nationality) VALUES ('Alan R. Moon', 'US');
INSERT INTO author(name, nationality) VALUES ('Vital Lacerda', 'PT');
INSERT INTO author(name, nationality) VALUES ('Simone Luciani', 'IT');
INSERT INTO author(name, nationality) VALUES ('Perepau Llistosella', 'ES');
INSERT INTO author(name, nationality) VALUES ('Michael Kiesling', 'DE');
INSERT INTO author(name, nationality) VALUES ('Phil Walker-Harding', 'US');

-- Mock of GAME:
INSERT INTO game(title, age, category_id, author_id) VALUES ('On Mars', '14', 1, 2);
INSERT INTO game(title, age, category_id, author_id) VALUES ('Aventureros al tren', '8', 3, 1);
INSERT INTO game(title, age, category_id, author_id) VALUES ('1920: Wall Street', '12', 1, 4);
INSERT INTO game(title, age, category_id, author_id) VALUES ('Barrage', '14', 1, 3);
INSERT INTO game(title, age, category_id, author_id) VALUES ('Los viajes de Marco Polo', '12', 1, 3);
INSERT INTO game(title, age, category_id, author_id) VALUES ('Azul', '8', 3, 5);

-- Mock of CLIENT:
INSERT INTO client(name) VALUES ('Cliente 1');
INSERT INTO client(name) VALUES ('Cliente 2');
INSERT INTO client(name) VALUES ('Cliente 3');
INSERT INTO client(name) VALUES ('Cliente 4');
INSERT INTO client(name) VALUES ('Cliente 5');
INSERT INTO client(name) VALUES ('Cliente 6');
INSERT INTO client(name) VALUES ('Cliente 7');

-- Mock of LOAN:
INSERT INTO loan(game_id, client_id, start_date, end_date) VALUES (1, 1, '2025-12-13', '2025-12-20');
INSERT INTO loan(game_id, client_id, start_date, end_date) VALUES (2, 2, '2025-06-05', '2025-06-12');
INSERT INTO loan(game_id, client_id, start_date, end_date) VALUES (3, 3, '2025-06-03', '2025-06-13');
INSERT INTO loan(game_id, client_id, start_date, end_date) VALUES (4, 4, '2025-06-08', '2025-06-20');
INSERT INTO loan(game_id, client_id, start_date, end_date) VALUES (5, 5, '2025-06-11', '2025-06-18');
INSERT INTO loan(game_id, client_id, start_date, end_date) VALUES (6, 6, '2025-06-07', '2025-06-17');