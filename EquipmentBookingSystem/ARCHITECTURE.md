# Kiến trúc Hệ thống Equipment Booking System

## 1. Tổng quan kiến trúc

```
┌─────────────────────────────────────────────────────────────┐
│                   Web Browser / Client                       │
│                                                              │
│                 http://localhost:8080                        │
└─────────────────────────────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                    Spring Boot Application                   │
│                                                              │
│  ┌───────────────────────────────────────────────────────┐  │
│  │  1. Controllers (Web Layer)                           │  │
│  │  - HomeController: Trang chủ & tìm kiếm              │  │
│  │  - AuthController: Đăng nhập/Đăng ký                │  │
│  │  - BookingController: Quản lý mượn/trả               │  │
│  │  - EquipmentController: Quản lý thiết bị (Admin)     │  │
│  │  - CategoryController: Quản lý danh mục (Admin)      │  │
│  │  - AdminController: Dashboard admin                   │  │
│  └───────────────────────────────────────────────────────┘  │
│                            │                                  │
│                            ▼                                  │
│  ┌───────────────────────────────────────────────────────┐  │
│  │  2. Services (Business Logic Layer)                   │  │
│  │  - AccountService: Quản lý tài khoản                 │  │
│  │  - EquipmentService: Quản lý thiết bị                │  │
│  │  - CategoryService: Quản lý danh mục                 │  │
│  │  - BookingService: Quản lý mượn/trả                  │  │
│  │    * Kiểm tra số lượng sẵn sàng                       │  │
│  │    * Cập nhật trạng thái thiết bị                    │  │
│  │    * Xử lý logic mượn/trả                            │  │
│  └───────────────────────────────────────────────────────┘  │
│                            │                                  │
│                            ▼                                  │
│  ┌───────────────────────────────────────────────────────┐  │
│  │  3. Repositories (Data Access Layer)                  │  │
│  │  - AccountRepository: JpaRepository<Account, Long>    │  │
│  │  - EquipmentRepository: JpaRepository<Equipment, ...> │  │
│  │  - CategoryRepository: JpaRepository<Category, ...>   │  │
│  │  - BookingRepository: JpaRepository<Booking, ...>     │  │
│  └───────────────────────────────────────────────────────┘  │
│                            │                                  │
│                            ▼                                  │
│  ┌───────────────────────────────────────────────────────┐  │
│  │  4. Models (Entity Layer)                             │  │
│  │  - Account: Người dùng/Admin                          │  │
│  │  - Equipment: Thiết bị                                │  │
│  │  - Category: Danh mục thiết bị                        │  │
│  │  - Booking: Lịch sử mượn/trả                          │  │
│  └───────────────────────────────────────────────────────┘  │
│                            │                                  │
│                            ▼                                  │
│  ┌───────────────────────────────────────────────────────┐  │
│  │  5. Configuration & Interceptors                      │  │
│  │  - SessionConfig: Cấu hình session                    │  │
│  │  - AdminInterceptor: Kiểm tra quyền admin            │  │
│  │  - WebConfig: Cấu hình Spring Web                    │  │
│  └───────────────────────────────────────────────────────┘  │
│                            │                                  │
└─────────────────────────────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                    MySQL Database                            │
│                                                              │
│  ┌──────────────────────────────────────────────────────┐  │
│  │ Tables:                                               │  │
│  │ - accounts: Tài khoản người dùng                     │  │
│  │ - categories: Danh mục thiết bị                      │  │
│  │ - equipment: Danh sách thiết bị                      │  │
│  │ - bookings: Lịch sử mượn/trả                         │  │
│  └──────────────────────────────────────────────────────┘  │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

## 2. Thành phần chính

### 2.1. Models (Entity Classes)

#### Account
```
Account
├── id: Long (PK)
├── username: String (Unique)
├── password: String
├── email: String (Unique)
├── fullName: String
├── role: Role (ADMIN, USER)
├── active: Boolean
├── createdAt: LocalDateTime
└── updatedAt: LocalDateTime
```

**Mối quan hệ**:
- 1 Account có nhiều Booking (1:N)

#### Category
```
Category
├── id: Long (PK)
├── name: String (Unique)
├── description: String
```

**Mối quan hệ**:
- 1 Category có nhiều Equipment (1:N)

#### Equipment
```
Equipment
├── id: Long (PK)
├── name: String
├── code: String (Unique)
├── category_id: Long (FK)
├── quantity: Integer
├── availableQuantity: Integer
├── description: String
├── status: EquipmentStatus (AVAILABLE, UNAVAILABLE, MAINTENANCE)
├── createdAt: LocalDateTime
└── updatedAt: LocalDateTime
```

**Mối quan hệ**:
- N Equipment thuộc 1 Category (N:1)
- 1 Equipment có nhiều Booking (1:N)

#### Booking
```
Booking
├── id: Long (PK)
├── account_id: Long (FK)
├── equipment_id: Long (FK)
├── quantity: Integer
├── borrowedAt: LocalDateTime
├── returnedAt: LocalDateTime
├── expectedReturnDate: LocalDateTime
├── status: BookingStatus (PENDING, APPROVED, BORROWED, RETURNED, REJECTED, OVERDUE)
├── notes: String
├── createdAt: LocalDateTime
└── updatedAt: LocalDateTime
```

**Mối quan hệ**:
- N Booking liên quan 1 Account (N:1)
- N Booking liên quan 1 Equipment (N:1)

### 2.2. Repositories (Data Access)

```
AccountRepository
├── findByUsername(String): Optional<Account>
├── findByEmail(String): Optional<Account>
└── existsByUsername(String): Boolean

CategoryRepository
└── findByName(String): Category

EquipmentRepository
├── findByCode(String): Optional<Equipment>
├── findByCategory(Category): List<Equipment>
├── findByStatus(Status): List<Equipment>
└── findByCategoryOrderByName(Category): List<Equipment>

BookingRepository
├── findByAccount(Account): List<Booking>
├── findByEquipment(Equipment): List<Booking>
├── findByStatus(BookingStatus): List<Booking>
└── findByAccountOrderByBorrowedAtDesc(Account): List<Booking>
```

### 2.3. Services (Business Logic)

#### AccountService
```
Chức năng:
- getAllAccounts()
- getAccountById(Long)
- getAccountByUsername(String)
- registerAccount(Account)
- login(String, String)
- saveAccount(Account)
- updateAccount(Long, Account)
- deleteAccount(Long)
```

#### CategoryService
```
Chức năng:
- getAllCategories()
- getCategoryById(Long)
- getCategoryByName(String)
- saveCategory(Category)
- updateCategory(Long, Category)
- deleteCategory(Long)
```

#### EquipmentService
```
Chức năng:
- getAllEquipment()
- getEquipmentById(Long)
- getEquipmentByCode(String)
- getEquipmentByCategory(Category)
- getAvailableEquipment()
- isEquipmentAvailable(Long, Integer): Boolean
- borrowEquipment(Long, Integer): void
  └─> Cập nhật availableQuantity
  └─> Cập nhật status thành UNAVAILABLE nếu hết
- returnEquipment(Long, Integer): void
  └─> Tăng availableQuantity
  └─> Cập nhật status thành AVAILABLE
- saveEquipment(Equipment)
- updateEquipment(Long, Equipment)
- deleteEquipment(Long)
```

#### BookingService
```
Chức năng:
- getAllBookings()
- getBookingById(Long)
- createBooking(Booking): Booking
  └─> Kiểm tra số lượng sẵn sàng
  └─> Gọi EquipmentService.borrowEquipment()
  └─> Lưu Booking với status BORROWED
- returnBooking(Long): Booking
  └─> Cập nhật status thành RETURNED
  └─> Gọi EquipmentService.returnEquipment()
- getBookingsByAccount(Account)
- getBookingsByEquipment(Equipment)
- getActiveBorrowings(Account)
```

### 2.4. Controllers (Web Layer)

#### HomeController
```
GET  /           → home(): Trang chủ
GET  /search     → search(): Tìm kiếm thiết bị
```

#### AuthController
```
GET  /auth/login     → showLoginForm(): Form đăng nhập
POST /auth/login     → login(): Xử lý đăng nhập
GET  /auth/register  → showRegisterForm(): Form đăng ký
POST /auth/register  → register(): Xử lý đăng ký
GET  /auth/logout    → logout(): Đăng xuất
```

#### BookingController
```
GET  /bookings/my-bookings              → myBookings(): Xem lịch sử mượn/trả
GET  /bookings/borrow/{equipmentId}     → showBorrowForm(): Form mượn
POST /bookings/borrow                   → borrowEquipment(): Xử lý mượn
GET  /bookings/return/{bookingId}       → returnEquipment(): Trả thiết bị
GET  /admin/bookings/all-bookings       → allBookings(): Xem tất cả (Admin)
```

#### EquipmentController (Admin)
```
GET  /admin/equipment         → listEquipment(): Danh sách
GET  /admin/equipment/add     → showAddEquipmentForm(): Form thêm
POST /admin/equipment/add     → addEquipment(): Thêm mới
GET  /admin/equipment/edit/{id}    → showEditEquipmentForm(): Form sửa
POST /admin/equipment/edit/{id}    → updateEquipment(): Cập nhật
GET  /admin/equipment/delete/{id}  → deleteEquipment(): Xóa
```

#### CategoryController (Admin)
```
GET  /admin/categories         → listCategories(): Danh sách
GET  /admin/categories/add     → showAddCategoryForm(): Form thêm
POST /admin/categories/add     → addCategory(): Thêm mới
GET  /admin/categories/edit/{id}    → showEditCategoryForm(): Form sửa
POST /admin/categories/edit/{id}    → updateCategory(): Cập nhật
GET  /admin/categories/delete/{id}  → deleteCategory(): Xóa
```

#### AdminController
```
GET /admin/dashboard   → dashboard(): Bảng điều khiển
GET /admin/logout      → logout(): Đăng xuất
```

### 2.5. Configuration

#### SessionConfig
- Thiết lập timeout session: 30 phút
- Quản lý lifecycle session

#### AdminInterceptor
- Kiểm tra user đang access `/admin/*`
- Nếu không phải admin → redirect `/`
- Nếu chưa login → redirect `/auth/login`

#### WebConfig
- Đăng ký AdminInterceptor
- Cấu hình các path patterns

## 3. Luồng xử lý chính

### 3.1. Luồng Mượn Thiết bị

```
User
  │
  └─> Chọn thiết bị & nhấn "Mượn"
      │
      └─> BookingController.showBorrowForm()
          │
          ├─> Kiểm tra user đã login?
          └─> Lấy Equipment từ database
              │
              └─> Hiển thị form với:
                  ├─ Tên thiết bị
                  ├─ Số lượng sẵn sàng
                  └─ Input form mượn
                      │
                      └─> POST /bookings/borrow
                          │
                          └─> BookingController.borrowEquipment()
                              │
                              └─> BookingService.createBooking()
                                  │
                                  ├─> EquipmentService.isEquipmentAvailable()
                                  │   └─> Kiểm tra availableQuantity >= requested
                                  │
                                  ├─> EquipmentService.borrowEquipment()
                                  │   ├─> Giảm availableQuantity
                                  │   └─> Cập nhật status nếu cần
                                  │
                                  ├─> Lưu Booking (status = BORROWED)
                                  │
                                  └─> Redirect /bookings/my-bookings
                                      └─> Hiển thị success message
```

### 3.2. Luồng Trả Thiết bị

```
User (trang /bookings/my-bookings)
  │
  └─> Nhấn "Trả" cho lần mượn
      │
      └─> BookingController.returnEquipment(bookingId)
          │
          └─> BookingService.returnBooking(bookingId)
              │
              ├─> Kiểm tra status == BORROWED?
              │
              ├─> Cập nhật returnedAt = now()
              │
              ├─> Cập nhật status = RETURNED
              │
              ├─> EquipmentService.returnEquipment()
              │   ├─> Tăng availableQuantity
              │   └─> Cập nhật status = AVAILABLE
              │
              ├─> Lưu Booking cập nhật
              │
              └─> Redirect /bookings/my-bookings
                  └─> Hiển thị success message
```

### 3.3. Luồng Admin Thêm Thiết bị

```
Admin
  │
  └─> Truy cập /admin/dashboard
      │
      └─> AdminController.dashboard()
          │
          └─> Nhấn "Quản lý thiết bị"
              │
              └─> GET /admin/equipment
                  │
                  └─> EquipmentController.listEquipment()
                      │
                      └─> Danh sách tất cả Equipment
                          │
                          └─> Nhấn "Thêm thiết bị mới"
                              │
                              └─> GET /admin/equipment/add
                                  │
                                  └─> EquipmentController.showAddEquipmentForm()
                                      │
                                      └─> Form với:
                                          ├─ Tên thiết bị
                                          ├─ Mã số
                                          ├─ Danh mục (dropdown)
                                          ├─ Số lượng
                                          └─ Mô tả
                                              │
                                              └─> POST /admin/equipment/add
                                                  │
                                                  └─> EquipmentController.addEquipment()
                                                      │
                                                      ├─> Validation
                                                      │   └─ Kiểm tra code độc nhất
                                                      │
                                                      ├─> EquipmentService.saveEquipment()
                                                      │   └─> Lưu vào database
                                                      │
                                                      └─> Redirect /admin/equipment
                                                          └─> Success message
```

## 4. Database Relations

```
┌──────────────┐         ┌──────────────────┐
│   accounts   │ 1    * │    bookings      │
└──────────────┘─────────└──────────────────┘
  PK: id                   FK: account_id
                            FK: equipment_id

┌──────────────┐         ┌──────────────────┐
│ categories   │ 1    * │    equipment     │
└──────────────┘─────────└──────────────────┘
  PK: id                   PK: id
                           FK: category_id

┌──────────────┐         ┌──────────────────┐
│  equipment   │ 1    * │    bookings      │
└──────────────┘─────────└──────────────────┘
  PK: id                   FK: equipment_id
```

## 5. Quy trình Quản lý Trạng thái

### Equipment Status
```
AVAILABLE (Sẵn sàng)
    └─> Khi:
        - availableQuantity > 0
        - Người dùng trả hết lượng mượn

UNAVAILABLE (Không sẵn sàng)
    └─> Khi:
        - availableQuantity = 0
        - Tất cả thiết bị đang được mượn

MAINTENANCE (Bảo trì)
    └─> Khi:
        - Admin đánh dấu cần bảo trì
        - Không cho mượn được
```

### Booking Status
```
PENDING (Chờ xét duyệt)
    └─> Chưa được admin phê duyệt

APPROVED (Đã phê duyệt)
    └─> Admin đã phê duyệt, sẵn sàng mượn

BORROWED (Đang mượn)
    └─> Người dùng đang mượn thiết bị

RETURNED (Đã trả)
    └─> Người dùng đã trả thiết bị

REJECTED (Từ chối)
    └─> Admin từ chối đơn mượn

OVERDUE (Quá hạn)
    └─> Người dùng không trả đúng thời hạn
```

## 6. Security & Authorization

### Session Management
- Sử dụng HttpSession
- Session timeout: 30 phút
- Session attribute: "user" (Account object)

### Authorization
- AdminInterceptor kiểm tra quyền trước khi truy cập `/admin/**`
- Controller kiểm tra `session.getAttribute("user")`
- Template kiểm tra `${session.user.role}`

### Roles
```
ADMIN
├─> Truy cập /admin/**
├─> Quản lý tất cả thiết bị
├─> Quản lý danh mục
├─> Xem tất cả lịch sử mượn/trả
└─> Quản lý tài khoản

USER
├─> Truy cập / (trang chủ)
├─> Xem danh sách thiết bị
├─> Mượn/Trả thiết bị
└─> Xem lịch sử riêng của mình
```

## 7. Validation Rules

### Account
- username: Bắt buộc, duy nhất, không được để trống
- password: Bắt buộc, không được để trống
- email: Bắt buộc, định dạng email hợp lệ, duy nhất
- fullName: Bắt buộc, không được để trống

### Category
- name: Bắt buộc, duy nhất, không được để trống

### Equipment
- name: Bắt buộc, không được để trống
- code: Bắt buộc, duy nhất, không được để trống
- quantity: >= 0
- availableQuantity: >= 0
- category: Bắt buộc

### Booking
- quantity: >= 1
- borrowedAt: Bắt buộc, mặc định = now()
- account: Bắt buộc
- equipment: Bắt buộc
- availableQuantity >= requested quantity

## 8. Performance Considerations

- Sử dụng connection pooling (HikariCP)
- Index trên: id, username, email, code, category_id, status
- Pagination (có thể thêm sau)
- Caching (có thể thêm sau)
- Lazy loading cho relationships

## 9. Mở rộng trong tương lai

```
Future Enhancements
├─> BCrypt password hashing
├─> Email notifications
├─> Report generation
├─> Loan deadline management
├─> Fine/penalty system
├─> Equipment maintenance tracking
├─> REST API endpoints
├─> Mobile app support
├─> QR code for equipment
├─> Advanced reporting
└─> Analytics dashboard
```

---

**Document Version**: 1.0
**Last Updated**: March 9, 2026
