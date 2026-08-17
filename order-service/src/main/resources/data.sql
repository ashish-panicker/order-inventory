INSERT INTO orders (order_id, customer_id, status, total_amount, is_deleted, created_at, updated_at) 
VALUES ('ORD-1001', 'CUST-1', 'CONFIRMED', 150.00, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO order_items (order_id, product_id, quantity, price, is_deleted, created_at, updated_at) 
VALUES (1, 'PROD-1', 1, 100.00, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO order_items (order_id, product_id, quantity, price, is_deleted, created_at, updated_at) 
VALUES (1, 'PROD-2', 1, 50.00, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
