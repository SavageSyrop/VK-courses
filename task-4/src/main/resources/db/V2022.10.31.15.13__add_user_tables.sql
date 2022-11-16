CREATE TABLE roles (
id BIGSERIAL NOT NULL,
name VARCHAR (128) NOT NULL,
CONSTRAINT roles_pk PRIMARY KEY (id)
);

CREATE TABLE permissions (
name VARCHAR (128) NOT NULL,
role_id bigint NOT NULL REFERENCES roles(id) ON DELETE NO ACTION
);

CREATE TABLE walls (
id BIGSERIAL NOT NULL,
post_permission VARCHAR (128) NOT NULL,
comment_permission VARCHAR (128) NOT NULL,
CONSTRAINT walls_pk PRIMARY KEY (id)
);

CREATE TABLE cities (
id BIGSERIAL PRIMARY KEY NOT NULL,
name VARCHAR (128) NOT NULL
);

CREATE TABLE users (
id BIGSERIAL NOT NULL,
username VARCHAR (128) NOT NULL,
password VARCHAR (128) NOT NULL,
email VARCHAR (128) NOT NULL,
first_name VARCHAR (128) NOT NULL,
last_name VARCHAR (128) NOT NULL,
gender_type VARCHAR (128) NOT NULL,
date_of_birth date NOT NULL,
city_id BIGSERIAL REFERENCES cities(id),
role_id bigint NOT NULL REFERENCES roles(id) ON DELETE NO ACTION,
wall_id bigint NOT NULL REFERENCES walls(id) ON DELETE CASCADE,
activation_code VARCHAR (255),
restore_password_code VARCHAR (255),
is_banned boolean NOT NULL DEFAULT false,
CONSTRAINT users_pk PRIMARY KEY (id)
);


alter TABLE users add column open_profile boolean;




