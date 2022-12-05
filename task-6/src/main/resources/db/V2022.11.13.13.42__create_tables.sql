create table product (
    code serial not null,
    name varchar (64) not null,
    CONSTRAINT product_pk PRIMARY KEY (code)
);

create table organisation (
    tax_number bigint primary key not null,
    name varchar (64) not null,
    checking_account bigint unique not null
);

create table receipt (
    id serial primary key not null,
    creation_date date not null,
    organisation_tax_number bigint references organisation(tax_number) not null
);

create table receipt_item (
    id serial primary key not null,
    receipt_id serial references receipt(id) not null,
    product_code serial references product(code) not null,
    price int not null,
    amount int not null
);