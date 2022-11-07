create TABLE roles (
id BIGSERIAL NOT NULL,
name varchar (32) NOT NULL,
CONSTRAINT roles_pk PRIMARY KEY (id)
);

create TABLE permissions (
id BIGSERIAL NOT NULL,
name varchar (32) NOT NULL,
role_id bigint REFERENCES roles(id),
CONSTRAINT permissions_pk PRIMARY KEY (id)
);

create TABLE user_infos (
id BIGSERIAL NOT NULL,
username varchar (64) NOT NULL,
password varchar (64) NOT NULL,
email varchar (64) NOT NULL,
first_name varchar (64) NOT NULL,
last_name varchar (64) NOT NULL,
gender_type varchar (64) NOT NULL,
date_of_birth date NOT NULL,
city varchar (64),
CONSTRAINT user_infos_pk PRIMARY KEY (id)
);


create TABLE walls (
id BIGSERIAL NOT NULL,
post_permission varchar (32) NOT NULL,
comment_permission varchar (32) NOT NULL,
CONSTRAINT walls_pk PRIMARY KEY (id)
);


create TABLE users (
id BIGSERIAL NOT NULL,
user_info_id bigint REFERENCES user_infos(id),
role_id bigint NOT NULL REFERENCES roles(id),
wall_id bigint NOT NULL REFERENCES walls(id),
activation_code varchar (255),
restore_password_code varchar (255),
is_banned boolean NOT NULL DEFAULT false,
CONSTRAINT users_pk PRIMARY KEY (id)
);




