CREATE TABLE IF NOT EXISTS inventory (
    product_id VARCHAR(255) PRIMARY KEY,
    quantity INT NOT NULL DEFAULT 0,
    in_stock BOOLEAN DEFAULT FALSE,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insert some sample data
INSERT INTO inventory (product_id, quantity, in_stock) VALUES 
('PROD-123', 100, TRUE),
('PROD-456', 50, TRUE),
('PROD-789', 0, FALSE);
