

INSERT INTO role (role_description,role_name) VALUES
('Shopper role','SHOPPER'),
('Default role for newly created record','CUSTOMER');

insert INTO user_address(add_id,address_line, street, city, state, pincode) VALUES
('address123', 'RK nagar', 'Madhapur', 'Vizag', 'TS', 500021);

INSERT INTO users (user_name, user_first_name, user_last_name, user_email, user_password, created_date, fk_add_id ) VALUES
('Shopper123','shop','per','shopper@mini.com','Shopper@pass',current_date(),'address123');

ALTER TABLE users ORDER BY internal_id;

INSERT INTO user_role(user_name, role_id) VALUES
((SELECT user_name from users where user_name = 'Shopper123'), (SELECT role_name from role where role_name = 'SHOPPER'));
