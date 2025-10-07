delete from users;
delete from role;

insert into role(role_name) values ('ADMIN'), ('CUSTOMER'), ('SELLER'), ('DELIVERY_AGENT');

-- Seed an admin user with a pre-hashed BCrypt password 'Admin#123' (example hash)
insert into users(name, email, password, phone, role_id)
values ('Admin', 'admin@example.com', '$2a$10$k8sQY3m2Gv3dWfW7C1Yw4O2m7kq8lGk9oV8jYwQd0o4O2q8N8m7yK', '0000000000', 1);

insert into product(name, description, price, stock) values
('Phone', 'Smart phone', 499.99, 100),
('Laptop', 'Gaming laptop', 1299.00, 50);

