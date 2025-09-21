-- Product Service Database Schema
-- Database: productdb

-- Products table
CREATE TABLE IF NOT EXISTS products (
    id BIGSERIAL PRIMARY KEY,
    sku VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(12,2) NOT NULL CHECK (price > 0),
    stock INTEGER NOT NULL CHECK (stock >= 0),
    image_url VARCHAR(500),
    category VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for products table
CREATE INDEX IF NOT EXISTS idx_product_sku ON products(sku);
CREATE INDEX IF NOT EXISTS idx_product_category ON products(category);
CREATE INDEX IF NOT EXISTS idx_product_stock ON products(stock);
CREATE INDEX IF NOT EXISTS idx_product_price ON products(price);
CREATE INDEX IF NOT EXISTS idx_product_created_at ON products(created_at);

-- Update trigger for updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_products_updated_at 
    BEFORE UPDATE ON products 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();

-- Insert sample products
INSERT INTO products (sku, name, description, price, stock, category, image_url) VALUES
('LAPTOP-001', 'MacBook Pro 16"', 'Apple MacBook Pro with M2 chip, 16GB RAM, 512GB SSD', 2499.99, 10, 'Electronics', 'https://example.com/macbook-pro.jpg'),
('PHONE-001', 'iPhone 15 Pro', 'Apple iPhone 15 Pro with A17 Pro chip, 128GB storage', 999.99, 25, 'Electronics', 'https://example.com/iphone-15.jpg'),
('BOOK-001', 'Clean Code', 'Clean Code: A Handbook of Agile Software Craftsmanship by Robert C. Martin', 29.99, 100, 'Books', 'https://example.com/clean-code.jpg'),
('CLOTH-001', 'Cotton T-Shirt', '100% Cotton Premium T-Shirt, Various Colors', 19.99, 50, 'Clothing', 'https://example.com/tshirt.jpg'),
('SHOE-001', 'Running Shoes', 'Professional Running Shoes, Size 7-12', 89.99, 30, 'Footwear', 'https://example.com/shoes.jpg')
ON CONFLICT (sku) DO NOTHING;