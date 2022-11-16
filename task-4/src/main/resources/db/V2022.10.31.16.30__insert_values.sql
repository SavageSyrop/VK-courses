INSERT INTO roles (name) values
('ADMIN'),
('USER');

INSERT INTO permissions (name, role_id) values
('WRITING', 1),
('WRITING', 2),
('ADMIN_ACTIONS', 1);


INSERT INTO walls(post_permission, comment_permission) values
('OWNER','OWNER'),
('FRIENDS','FRIENDS'),
('ALL','ALL'),
('OWNER','FRIENDS'),
('FRIENDS','OWNER'),
('OWNER','ALL');

INSERT INTO cities(name) values
('Tomsk'),
('Moscow'),
('Greenbow'),
('Las-Vegas'),
('Vinnitsa'),
('Citadel');

INSERT INTO users(username, password, email, first_name, last_name, gender_type, date_of_birth, city_id, open_profile, role_id, wall_id, activation_code, is_banned) values
('desertfox', '$2a$10$dCKE0qv1SW3dKBTXkauFburkrCGOznBAhdXaV3Km9yre7qysphk1u','fess.2002@mail.ru', 'Yaroslav', 'Shevtsov', 'MALE', '09.11.2001', 1, true , 1, 1, null, false),
('aleoonka', '$2a$10$Uimw7bv5iTa.5miRSn4M4uGosxfyh1d89aVEHIkSNsPFkz6NmgOLq','project.sunshine@mail.ru', 'Alena', 'Tsemkalo', 'FEMALE', '23.07.2002', 2, true, 2, 2,NULL, false),
('Gump', '$2a$10$s.HMSOhwXDkV6aK87ArD2.SBooCd4S9T1pAtMF.GygkMPRZaldyDK','project.sunshine@mail.ru', 'Forrest', 'Gump', 'KROGAN', '09.05.1945', 3, true, 2, 3, NULL, false),
('Belka','$2a$10$R0o2eqVOcKpt.320Dr6rqujuXTSV87Pt.o0NydQafz3kDIzPnpzAO','project.sunshine@mail.ru','Squirel','Thompson','FEMALE', '07.09.2022',4, false, 2, 4, NULL, false),
('BOT','$2a$10$ksNrhvJfhTKJ46mz2wLpa.dDJMjs0PtPh1tr7TjkqNRUwvcsPhNMO','project.sunshine@mail.ru','Roflan','Rabotyaga','KROGAN', '01.01.2000',5, false, 1, 5, NULL, false),
('Reaper','$2a$10$O1alPtSNaqQ1FXvEGc/Y/.VJb9N2rZbRNN72uDEhXd4FLGO/isPZy','project.sunshine@mail.ru','The','Sovereign','MALE', '13.03.37',6, true, 2, 6, NULL, false);


INSERT INTO friend_requests (sender_id, recipient_id, is_accepted) values
(1,2,true),
(1,5,true),
(2,1,true),
(2,4,false),
(3,6,true),
(3,2,false),
(4,6,false),
(4,3,false),
(5,1,true),
(5,2,false),
(6,2,false),
(6,3,true);

INSERT INTO chats (name, chat_type, owner_id) values
('Founders', 1, 1),
('Gump / Reaper', 2, NULL);


INSERT INTO messages (sender_id, message_text, sending_time, chat_id) values
(1,'Хай, я создал чат с основателями!','2022-09-15 23:32:12',1),
(1,'Сегодня 15.09.2022, мы уложились в срок!','2022-09-15 23:32:34',1),
(2,'Ураааааа!!! Куда пойдем отмечать?','2022-09-15 23:32:50',1),
(5,'ОШиБкаАаааА, работа никогда не кончается!','2022-09-15 23:33:22',1),
(2,':(','2022-09-15 23:33:34',1),
(3,'Привет, я лесной болван!','2022-09-15 23:34:57',2),
(6,'This form is redundant!','2022-09-15 23:35:21',2);

INSERT INTO chat_participations (user_id, chat_id) values
(1,1),
(2,1),
(5,1),
(3,2),
(6,2);

INSERT INTO user_groups(name, info, wall_type, open_to_join) values
('Welcome!','Hello world!','ALL',true),
('Admin group','Closed','ADMINS',false),
('Predloghka','Write and we will post!','OFFERED_POSTS',true);

INSERT INTO group_memberships (user_id, group_id, group_role) values
(6,1,'OWNER'),
(1,2,'OWNER'),
(5,2,'ADMIN'),
(3,2,'AWAITING_CHECK'),
(2,3,'OWNER'),
(4,3,'SUBSCRIBER'),
(2,1,'SUBSCRIBER');


INSERT INTO posts(post_text, sending_time, author_id, group_id, wall_id, is_published) values
('We impose order on the chaos of organic evolution. You exist because we allow it, and you will end because we demand it!','2022-09-16 19:47:15',6,1,NULL,true),
('Что за фанатик ведет эту группу?','2022-09-16 19:48:02',4,1,NULL,true),
('СЛАВСЯ ВЕЛИКИЙ ВЛАСТЕЛИН! УДАР!','2022-09-16 19:48:24',2,1,NULL,true),
('Отчёт BOT: В строю','2022-09-16 19:51:47',5,2,NULL,true),
('Привет, солнышки!\nДелитесь своими увлекательными историями, оставляя их в предложенных постах, а я их опубликую!','2022-09-16 19:55:59',2,3,NULL,true),
('Я маслопит!','2022-09-16 19:59:28',4,3,NULL,false),
('Assuming direct control','2022-09-16 20:49:32',6,NULL,6,true),
('desertfox был здесь','2022-09-16 21:14:23',1,NULL,2,true);

INSERT INTO comments (author_id, comment_text, sending_time, post_id) values
(6,'You cannot resist!','2022-09-16 20:29:06',2),
(1,'Тебе там не темно?','2022-09-16 20:32:24',3),
(1,'Принято, BOT!','2022-09-16 20:33:26',4),
(4,'Круто, сейчас подам новость!','2022-09-16 20:36:02',5),
(4,'Сгинь!','2022-09-16 20:51:46',7),
(2,'aleoonka была здесь','2022-09-16 21:15:04',8);

INSERT INTO likes (user_id, sending_time, comment_id, post_id) values
(2,'2022-09-16 20:27:04',NULL,1),
(1,'2022-09-16 20:27:20',NULL,1),
(6,'2022-09-16 20:27:25',NULL,1),
(3,'2022-09-16 20:27:57',NULL,2),
(2,'2022-09-16 20:29:51',1,NULL),
(5,'2022-09-16 20:33:48',3,NULL),
(1,'2022-09-16 20:35:19',NULL,5),
(4,'2022-09-16 20:35:26',NULL,5),
(2,'2022-09-16 20:52:02',NULL,7),
(6,'2022-09-16 20:52:08',NULL,7),
(1,'2022-09-16 20:53:03',NULL,7),
(2,'2022-09-16 20:53:38',5,NULL),
(1,'2022-09-16 20:53:49',5,NULL),
(2,'2022-09-16 21:15:12',NULL,8),
(1,'2022-09-16 21:15:46',6,NULL);