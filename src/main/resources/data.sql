INSERT INTO customer (id, name) VALUES (1, 'Shalini');
INSERT INTO customer (id, name) VALUES (2, 'Murali');
INSERT INTO customer (id, name) VALUES (3, 'Jane');

-- Shalini transactions
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (1, 120, '2025-08-15');
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (1, 75, '2025-09-01');
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (1, 120, '2026-08-15');
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (1, 75, '2026-09-01');
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (1, 100, '2026-07-01');
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (1, 150, '2026-07-15');

-- Murali transactions
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (2, 200, '2025-10-10');
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (2, 350, '2026-09-05');
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (2, 120, '2025-07-10');
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (2, 300, '2026-08-05');