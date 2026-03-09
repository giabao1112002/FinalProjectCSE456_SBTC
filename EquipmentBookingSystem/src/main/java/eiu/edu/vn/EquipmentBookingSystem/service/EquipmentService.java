package eiu.edu.vn.EquipmentBookingSystem.service;

import eiu.edu.vn.EquipmentBookingSystem.model.Equipment;
import eiu.edu.vn.EquipmentBookingSystem.model.Category;
import eiu.edu.vn.EquipmentBookingSystem.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EquipmentService {
    @Autowired
    private EquipmentRepository equipmentRepository;

    public List<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();
    }

    public Optional<Equipment> getEquipmentById(Long id) {
        return equipmentRepository.findById(id);
    }

    public Equipment saveEquipment(Equipment equipment) {
        if (equipment.getAvailableQuantity() == null) {
            equipment.setAvailableQuantity(equipment.getQuantity());
        }
        equipment.setUpdatedAt(LocalDateTime.now());
        return equipmentRepository.save(equipment);
    }

    public void deleteEquipment(Long id) {
        equipmentRepository.deleteById(id);
    }

    public Optional<Equipment> getEquipmentByCode(String code) {
        return equipmentRepository.findByCode(code);
    }

    public List<Equipment> getEquipmentByCategory(Category category) {
        return equipmentRepository.findByCategoryOrderByName(category);
    }

    public List<Equipment> getAvailableEquipment() {
        return equipmentRepository.findByStatus(Equipment.EquipmentStatus.AVAILABLE);
    }

    // Kiểm tra xem thiết bị có đủ số lượng để mượn không
    public boolean isEquipmentAvailable(Long equipmentId, Integer requestedQuantity) {
        Optional<Equipment> equipment = equipmentRepository.findById(equipmentId);
        if (equipment.isPresent()) {
            return equipment.get().getAvailableQuantity() >= requestedQuantity;
        }
        return false;
    }

    // Cập nhật số lượng sẵn sàng khi mượn
    public void borrowEquipment(Long equipmentId, Integer quantity) {
        Optional<Equipment> equipment = equipmentRepository.findById(equipmentId);
        if (equipment.isPresent()) {
            Equipment eq = equipment.get();
            eq.setAvailableQuantity(eq.getAvailableQuantity() - quantity);
            if (eq.getAvailableQuantity() == 0) {
                eq.setStatus(Equipment.EquipmentStatus.UNAVAILABLE);
            }
            eq.setUpdatedAt(LocalDateTime.now());
            equipmentRepository.save(eq);
        }
    }

    // Cập nhật số lượng sẵn sàng khi trả
    public void returnEquipment(Long equipmentId, Integer quantity) {
        Optional<Equipment> equipment = equipmentRepository.findById(equipmentId);
        if (equipment.isPresent()) {
            Equipment eq = equipment.get();
            eq.setAvailableQuantity(eq.getAvailableQuantity() + quantity);
            eq.setStatus(Equipment.EquipmentStatus.AVAILABLE);
            eq.setUpdatedAt(LocalDateTime.now());
            equipmentRepository.save(eq);
        }
    }

    public void updateEquipment(Long id, Equipment equipment) {
        Optional<Equipment> existingEquipment = equipmentRepository.findById(id);
        if (existingEquipment.isPresent()) {
            Equipment eq = existingEquipment.get();
            eq.setName(equipment.getName());
            eq.setCode(equipment.getCode());
            eq.setCategory(equipment.getCategory());
            eq.setQuantity(equipment.getQuantity());
            eq.setDescription(equipment.getDescription());
            eq.setStatus(equipment.getStatus());
            eq.setUpdatedAt(LocalDateTime.now());
            equipmentRepository.save(eq);
        }
    }
}
