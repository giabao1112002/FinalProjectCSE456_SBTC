package eiu.edu.vn.EquipmentBookingSystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "equipment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên thiết bị không được để trống")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Mã số thiết bị không được để trống")
    @Column(unique = true, nullable = false)
    private String code;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Min(value = 0, message = "Số lượng phải >= 0")
    @Column(nullable = false)
    private Integer quantity = 0;

    @Min(value = 0, message = "Số lượng sẵn sàng phải >= 0")
    @Column(nullable = false)
    private Integer availableQuantity = 0;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EquipmentStatus status = EquipmentStatus.AVAILABLE;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public enum EquipmentStatus {
        AVAILABLE,      // Sẵn sàng
        UNAVAILABLE,    // Không sẵn sàng
        MAINTENANCE     // Bảo trì
    }
}
