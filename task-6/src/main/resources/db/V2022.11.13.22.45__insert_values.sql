insert into product (name) values
('Хлеб белый'),
('Хлеб черный'),
('Хлеб коричневый'),
('Хлеб сладкий'),
('Хлеб жидкий'),
('Вяземский пряник'),
('Хлеб пожилой'),
('Хлеб удаляемый');


insert into organisation(tax_number, name, checking_account) VALUES
(21632632,'Калининградский хлебокомбинат',72543320),
(16041604,'Томский хлебокомбинат',20222022),
(43632466,'Вяземский хлебокомбинат',84398890),
(26636366,'Владимирский хлебокомбинат',12345678),
(34624326,'Кемеровский хлебокомбинат',87654321),
(86585668,'Барнаульский хлебокомбинат',23456789),
(45757545,'Московский хлебокомбинат',98765432),
(34575434,'Воронежский хлебокомбинат',34567890),
(34689299,'Самарский хлебокомбинат',19847654),
(64789298,'Смоленский хлебокомбинат',43634466),
(64392994,'Новосибирский хлебокомбинат',98436996);


insert into receipt (creation_date, organisation_tax_number) VALUES
('2022-11-11',21632632),
('2022-11-12',16041604),
('2022-11-13',43632466),
('2022-11-14',26636366),
('2022-11-15',34624326),
('2022-11-16',86585668),
('2022-11-17',45757545),
('2022-11-18',34575434),
('2022-11-19',34689299),
('2022-11-20',64789298),
('2022-11-21',64789298);


insert into receipt_item(receipt_id, product_code, price, amount) VALUES
(1,1,20,40),
(1,2,30,30),
(2,2,40,40),
(2,3,50,50),
(3,3,60,60),
(3,4,70,20),
(4,4,80,80),
(4,5,90,90),
(5,5,100,60),
(5,6,10,30),
(6,6,20,20),
(6,7,30,70),
(7,7,40,40),
(7,1,50,50),
(8,2,60,60),
(8,3,70,70),
(9,4,80,30),
(9,5,90,40),
(10,6,100,100),
(10,7,200,200);