package eiu.edu.vn.EquipmentBookingSystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;

    @Min(value = 1, message = "Số lượng mượn phải >= 1")
    @Column(nullable = false)
    private Integer quantity = 1;

    @Column(nullable = false)
    private LocalDateTime borrowedAt = LocalDateTime.now();

    @Column
    private LocalDateTime returnedAt;

    @Column
    private LocalDateTime expectedReturnDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.BORROWED;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public enum BookingStatus {
        PENDING,        // Chờ xét duyệt
        APPROVED,       // Đã phê duyệt
        BORROWED,       // Đang mượn
        RETURNED,       // Đã trả
        REJECTED,       // Từ chối
        OVERDUE         // Quá hạn
    }
}
