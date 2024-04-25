CREATE DATABASE IF NOT EXISTS product
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

use product;
CREATE TABLE IF NOT EXISTS product (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         product_name VARCHAR(255) NOT NULL,
                         price DECIMAL(10, 2) NOT NULL,
                         description TEXT,
                         stock INT NOT NULL,
                         image_urls TEXT
);

CREATE TABLE IF NOT EXISTS orders (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        product_id INT NOT NULL,
                        quantity INT NOT NULL,
                        total_price DECIMAL(10, 2) NOT NULL,
                        status VARCHAR(50) NOT NULL
);

-- 清除商品表中的数据
DELETE FROM product where 1 = 1;

-- 插入新的商品数据
INSERT INTO product (product_name, price, description, stock, image_urls)
VALUES
    ('iphone 15', 4999.00, 'Latest iPhone model', 20, 'image1.jpg,image2.jpg'),
    ('小米13', 2999.00, 'Latest Xiaomi model', 20, 'image3.jpg,image4.jpg');


