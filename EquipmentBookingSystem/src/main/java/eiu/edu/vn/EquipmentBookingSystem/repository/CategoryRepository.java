package eiu.edu.vn.EquipmentBookingSystem.repository;

import eiu.edu.vn.EquipmentBookingSystem.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
}
