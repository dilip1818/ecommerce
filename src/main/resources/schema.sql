drop table if exists order_item;
drop table if exists payment;
drop table if exists orders;
drop table if exists product;
drop table if exists users;
drop table if exists role;

create table role (
  role_id serial primary key,
  role_name varchar(50) not null unique
);

create table users (
  user_id serial primary key,
  name varchar(100) not null,
  email varchar(150) not null unique,
  password varchar(200) not null,
  phone varchar(30),
  role_id int references role(role_id)
);

create table product (
  product_id serial primary key,
  name varchar(150) not null,
  description text,
  price numeric(12,2) not null,
  stock int not null,
  seller_id int references users(user_id)
);

create table orders (
  order_id serial primary key,
  user_id int not null references users(user_id),
  order_date timestamp not null,
  status varchar(30) not null,
  total_amount numeric(12,2) not null,
  delivery_agent_id int references users(user_id)
);

create table order_item (
  order_item_id serial primary key,
  order_id int not null references orders(order_id) on delete cascade,
  product_id int not null references product(product_id),
  quantity int not null,
  price numeric(12,2) not null
);

create table payment (
  payment_id serial primary key,
  order_id int not null unique references orders(order_id) on delete cascade,
  payment_date timestamp not null,
  amount numeric(12,2) not null,
  method varchar(30) not null
);



