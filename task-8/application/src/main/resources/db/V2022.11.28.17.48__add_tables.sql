create table company (
    id serial primary key,
    name varchar unique not null
);

create table product (
    id serial primary key,
    name varchar not null,
    company_name varchar not null,
    amount int not null
);


create table users (
    id serial primary key,
    name varchar unique not null,
    password varchar not null
);

create table role (
    id serial primary key,
    name varchar not null
);

create table user_role (
    user_id int not null,
    role_id int not null,
    primary key (user_id, role_id)
);

