insert into company (name) values
('OAO Jmih'),
('Bogdan');

insert into product (name, company_name, amount) values
('racoon', 'OAO Jmih', 54),
('wrench', 'Bogdan', 9);

insert into users (name, password) values
('manager', 'manager'),
('guest', 'guest');

insert into role (name) values
('Manager'),
('Guest');

insert into user_role (user_id, role_id) values
(1,1),
(1,2),
(2,2);
