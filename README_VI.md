# Equipment Booking System

Hệ thống quản lý mượn/trả thiết bị học tập cho cơ sở vật chất (CSE 456 Group Project).

## Tính năng chính

### Cho người dùng (User):
- ✅ Đăng ký tài khoản mới
- ✅ Đăng nhập/Đăng xuất
- ✅ Xem danh sách thiết bị có sẵn
- ✅ Tìm kiếm thiết bị theo danh mục hoặc từ khóa
- ✅ Mượn thiết bị
- ✅ Quản lý lịch sử mượn/trả của mình

### Cho quản trị viên (Admin):
- ✅ Bảng điều khiển (Dashboard) với thống kê
- ✅ Quản lý danh mục thiết bị (Thêm/Sửa/Xóa)
- ✅ Quản lý thiết bị (Thêm/Sửa/Xóa/Cập nhật trạng thái)
- ✅ Quản lý lịch sử mượn/trả của tất cả người dùng
- ✅ Quản lý tài khoản người dùng

## Công nghệ sử dụng

- **Backend**: Spring Boot 4.0.3
- **Database**: MySQL 8.0+
- **Frontend**: Thymeleaf + Bootstrap 5
- **ORM**: Spring Data JPA + Hibernate
- **Validation**: Jakarta Validation
- **Build**: Maven 3.9.5
- **Java**: JDK 25

## Cấu trúc Project

```
EquipmentBookingSystem/
├── src/
│   ├── main/
│   │   ├── java/eiu/edu/vn/EquipmentBookingSystem/
│   │   │   ├── model/              # Entity Models
│   │   │   ├── repository/         # JPA Repositories
│   │   │   ├── service/            # Business Logic
│   │   │   ├── controller/         # Controllers
│   │   │   ├── config/             # Configuration Classes
│   │   │   └── EquipmentBookingSystemApplication.java
│   │   └── resources/
│   │       ├── templates/          # Thymeleaf Templates
│   │       └── application.properties
│   └── test/
│       └── java/                   # Unit Tests
├── pom.xml                         # Maven Configuration
└── HELP.md
```

## Hướng dẫn chạy project

### 1. Chuẩn bị môi trường
- Cài đặt JDK 25
- Cài đặt MySQL 8.0+
- Cài đặt Maven 3.9.5

### 2. Thiết lập Database
```sql
CREATE DATABASE IF NOT EXISTS cse456_group_project;
USE cse456_group_project;
```

### 3. Cấu hình kết nối Database
Sửa file `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/cse456_group_project
spring.datasource.username=root
spring.datasource.password=root
```

### 4. Build Project
```bash
mvn clean package
```

### 5. Chạy Application
```bash
mvn spring-boot:run
```

Hoặc chạy JAR file:
```bash
java -jar target/EquipmentBookingSystem-0.0.1-SNAPSHOT.jar
```

### 6. Truy cập ứng dụng
- **URL**: http://localhost:8080
- **Tài khoản Admin mặc định**: (Cần tạo trong database hoặc qua register)

## Models (Entity Classes)

### Category
- id: Long (Primary Key)
- name: String (Unique, Not Null)
- description: String (Optional)

### Equipment
- id: Long (Primary Key)
- name: String (Not Null)
- code: String (Unique, Not Null)
- category: Category (Foreign Key)
- quantity: Integer (Tổng số lượng)
- availableQuantity: Integer (Số lượng sẵn sàng)
- description: String (Optional)
- status: EquipmentStatus (AVAILABLE, UNAVAILABLE, MAINTENANCE)
- createdAt: LocalDateTime
- updatedAt: LocalDateTime

### Account
- id: Long (Primary Key)
- username: String (Unique, Not Null)
- password: String (Not Null)
- email: String (Unique, Not Null)
- fullName: String (Not Null)
- role: Role (ADMIN, USER)
- active: Boolean
- createdAt: LocalDateTime
- updatedAt: LocalDateTime

### Booking
- id: Long (Primary Key)
- account: Account (Foreign Key)
- equipment: Equipment (Foreign Key)
- quantity: Integer (Số lượng mượn)
- borrowedAt: LocalDateTime
- returnedAt: LocalDateTime (Optional)
- expectedReturnDate: LocalDateTime (Optional)
- status: BookingStatus (PENDING, APPROVED, BORROWED, RETURNED, REJECTED, OVERDUE)
- notes: String (Optional)
- createdAt: LocalDateTime
- updatedAt: LocalDateTime

## API Endpoints

### Authentication
- `GET /auth/login` - Trang đăng nhập
- `POST /auth/login` - Xử lý đăng nhập
- `GET /auth/register` - Trang đăng ký
- `POST /auth/register` - Xử lý đăng ký
- `GET /auth/logout` - Đăng xuất

### Home & Search
- `GET /` - Trang chủ
- `GET /search` - Tìm kiếm thiết bị

### Bookings
- `GET /bookings/my-bookings` - Xem lịch sử mượn/trả của mình
- `GET /bookings/borrow/{equipmentId}` - Form mượn thiết bị
- `POST /bookings/borrow` - Xử lý mượn thiết bị
- `GET /bookings/return/{bookingId}` - Trả thiết bị

### Admin Dashboard
- `GET /admin/dashboard` - Bảng điều khiển admin
- `GET /admin/logout` - Đăng xuất admin

### Equipment Management (Admin)
- `GET /admin/equipment` - Danh sách thiết bị
- `GET /admin/equipment/add` - Form thêm thiết bị
- `POST /admin/equipment/add` - Thêm thiết bị mới
- `GET /admin/equipment/edit/{id}` - Form sửa thiết bị
- `POST /admin/equipment/edit/{id}` - Cập nhật thiết bị
- `GET /admin/equipment/delete/{id}` - Xóa thiết bị

### Category Management (Admin)
- `GET /admin/categories` - Danh sách danh mục
- `GET /admin/categories/add` - Form thêm danh mục
- `POST /admin/categories/add` - Thêm danh mục mới
- `GET /admin/categories/edit/{id}` - Form sửa danh mục
- `POST /admin/categories/edit/{id}` - Cập nhật danh mục
- `GET /admin/categories/delete/{id}` - Xóa danh mục

### Bookings Management (Admin)
- `GET /admin/bookings/all-bookings` - Xem tất cả lịch sử mượn/trả

## Hướng dẫn sử dụng

### Cho người dùng:
1. Truy cập trang chủ
2. Nếu chưa có tài khoản, hãy đăng ký một tài khoản mới
3. Đăng nhập bằng username và password
4. Tìm kiếm thiết bị cần mượn
5. Nhấn nút "Mượn" để bắt đầu quy trình mượn
6. Xem lịch sử mượn/trả trong mục "Mượn/Trả của tôi"
7. Trả thiết bị khi không còn cần sử dụng

### Cho quản trị viên:
1. Đăng nhập với tài khoản admin
2. Truy cập "Quản lý" để vào dashboard
3. Quản lý danh mục (thêm/sửa/xóa)
4. Quản lý thiết bị (thêm/sửa/xóa/cập nhật số lượng)
5. Xem lịch sử mượn/trả của tất cả người dùng
6. Theo dõi thống kê về thiết bị và tài khoản

## Lưu ý quan trọng

- Hệ thống sử dụng session để quản lý đăng nhập, session timeout: 30 phút
- Chỉ admin mới có thể truy cập bảng điều khiển quản lý
- Mật khẩu chưa được mã hóa (TODO: Sử dụng BCrypt)
- Cần thiết lập dữ liệu ban đầu (danh mục, thiết bị) từ admin dashboard

## Troubleshooting

### Lỗi kết nối Database
- Kiểm tra MySQL server có chạy không
- Kiểm tra username/password trong application.properties
- Đảm bảo database `cse456_group_project` tồn tại

### Lỗi Port 8080 đã sử dụng
- Đổi port trong application.properties: `server.port=8081`

### Lỗi Thymeleaf không tìm thấy templates
- Kiểm tra các template HTML nằm trong `src/main/resources/templates/`
- Tên file phải khớp với return value trong controller

## Yêu cầu nâng cấp (Future Features)

- ✏️ Mã hóa mật khẩu bằng BCrypt
- ✏️ Hệ thống thông báo qua email
- ✏️ Báo cáo thống kê chi tiết
- ✏️ Quản lý hạn trị hạn mượn
- ✏️ Hệ thống phân quyền chi tiết
- ✏️ API REST cho mobile app

## Liên hệ & Support

**Project**: Equipment Booking System - CSE 456 Group Project
**Group**: Final Project for CSE 456
**Academic Year**: 2025-2026

---

**Last Updated**: March 9, 2026
