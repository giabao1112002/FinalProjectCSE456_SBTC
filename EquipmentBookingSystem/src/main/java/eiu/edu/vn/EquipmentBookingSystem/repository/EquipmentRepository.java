package eiu.edu.vn.EquipmentBookingSystem.repository;

import eiu.edu.vn.EquipmentBookingSystem.model.Equipment;
import eiu.edu.vn.EquipmentBookingSystem.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    Optional<Equipment> findByCode(String code);
    List<Equipment> findByCategory(Category category);
    List<Equipment> findByStatus(Equipment.EquipmentStatus status);
    List<Equipment> findByCategoryOrderByName(Category category);
}
