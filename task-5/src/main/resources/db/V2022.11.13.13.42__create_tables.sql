create table products (
    code serial not null,
    name varchar (64) not null,
    CONSTRAINT products_pk PRIMARY KEY (code)
);

create table organisations (
    tax_number bigint primary key not null,
    name varchar (64) not null,
    checking_account bigint unique not null
);

create table receipts (
    id serial primary key not null,
    creation_date date not null,
    organisation_tax_number bigint references organisations(tax_number) not null
);

create table receipt_items (
    id serial primary key not null,
    receipt_id serial references receipts(id) not null,
    product_code serial references products(code),
    price int not null,
    amount int not null
);