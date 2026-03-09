-- ===============================================
-- Equipment Booking System - Database Schema
-- ===============================================

-- Tạo database
CREATE DATABASE IF NOT EXISTS cse456_group_project;
USE cse456_group_project;

-- Bảng Categories (Danh mục thiết bị)
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description LONGTEXT,
    INDEX idx_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng Accounts (Tài khoản)
CREATE TABLE IF NOT EXISTS accounts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    full_name VARCHAR(255) NOT NULL,
    role VARCHAR(50) DEFAULT 'USER',
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng Equipment (Thiết bị)
CREATE TABLE IF NOT EXISTS equipment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(255) NOT NULL UNIQUE,
    category_id BIGINT NOT NULL,
    quantity INT DEFAULT 0,
    available_quantity INT DEFAULT 0,
    description LONGTEXT,
    status VARCHAR(50) DEFAULT 'AVAILABLE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE,
    INDEX idx_code (code),
    INDEX idx_category (category_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng Bookings (Mượn/Trả)
CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_id BIGINT NOT NULL,
    equipment_id BIGINT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    borrowed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    returned_at TIMESTAMP NULL,
    expected_return_date TIMESTAMP NULL,
    status VARCHAR(50) DEFAULT 'BORROWED',
    notes LONGTEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE,
    FOREIGN KEY (equipment_id) REFERENCES equipment(id) ON DELETE CASCADE,
    INDEX idx_account (account_id),
    INDEX idx_equipment (equipment_id),
    INDEX idx_status (status),
    INDEX idx_borrowed_at (borrowed_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ===============================================
-- Dữ liệu khởi tạo (Initial Data)
-- ===============================================

-- Chèn danh mục mẫu
INSERT INTO categories (name, description) VALUES
('Laptop', 'Máy tính xách tay cho học tập'),
('Máy chiếu', 'Máy chiếu cho các buổi thuyết trình'),
('Micro', 'Microphone cho sự kiện'),
('Loa', 'Loa công suất lớn'),
('Máy tính để bàn', 'PC cho phòng lab'),
('Tablet', 'Máy tính bảng'),
('Camera', 'Máy ảnh/camera quay phim'),
('Ổ cứng ngoài', 'Ổ cứng SSD/HDD ngoài'),
('Bàn phím', 'Bàn phím cơ học'),
('Chuột', 'Chuột không dây');

-- Chèn tài khoản admin mẫu
INSERT INTO accounts (username, password, email, full_name, role, active, created_at, updated_at) 
VALUES 
('admin', 'admin123', 'admin@eiu.edu.vn', 'Administrator', 'ADMIN', TRUE, NOW(), NOW());
-- Chèn thiết bị mẫu
INSERT INTO equipment (name, code, category_id, quantity, available_quantity, description, status) VALUES
('Dell XPS 13', 'LAPTOP-001', 1, 5, 3, 'Laptop cao cấp cho học tập lập trình', 'AVAILABLE'),
('Dell XPS 15', 'LAPTOP-002', 1, 3, 2, 'Laptop gaming với card đồ họa', 'AVAILABLE'),
('MacBook Pro', 'LAPTOP-003', 1, 2, 1, 'Laptop Apple cho các khóa học iOS', 'AVAILABLE'),
('Epson EB-X06', 'PROJECTOR-001', 2, 4, 3, 'Máy chiếu độ sáng 3500 lumens', 'AVAILABLE'),
('Sony VPL-FX30', 'PROJECTOR-002', 2, 2, 1, 'Máy chiếu LED cho lớp học', 'AVAILABLE'),
('Audio-Technica AT4040', 'MICROPHONE-001', 3, 10, 7, 'Condenser microphone chuyên nghiệp', 'AVAILABLE'),
('Shure SM58', 'MICROPHONE-002', 3, 5, 3, 'Microphone động cho sự kiện', 'AVAILABLE'),
('JBL PartyBox 110', 'SPEAKER-001', 4, 3, 2, 'Loa karaoke di động', 'AVAILABLE'),
('Intel i7-13700K PC', 'DESKTOP-001', 5, 2, 1, 'PC Gaming cấu hình cao', 'AVAILABLE'),
('iPad Pro 12.9', 'TABLET-001', 6, 4, 3, 'Tablet Apple cho thiết kế', 'AVAILABLE');

-- ===============================================
-- Kiểm tra dữ liệu
-- ===============================================
-- SELECT * FROM categories;
-- SELECT * FROM accounts;
-- SELECT * FROM equipment;
-- SELECT COUNT(*) as total_equipment FROM equipment;
