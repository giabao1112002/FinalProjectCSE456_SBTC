package eiu.edu.vn.EquipmentBookingSystem.controller;

import eiu.edu.vn.EquipmentBookingSystem.model.Equipment;
import eiu.edu.vn.EquipmentBookingSystem.model.Category;
import eiu.edu.vn.EquipmentBookingSystem.service.EquipmentService;
import eiu.edu.vn.EquipmentBookingSystem.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/equipment")
public class EquipmentController {
    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String listEquipment(Model model) {
        model.addAttribute("equipmentList", equipmentService.getAllEquipment());
        return "admin/equipment/list";
    }

    @GetMapping("/add")
    public String showAddEquipmentForm(Model model) {
        model.addAttribute("equipment", new Equipment());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/equipment/form";
    }

    @PostMapping("/add")
    public String addEquipment(@Valid @ModelAttribute Equipment equipment, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "admin/equipment/form";
        }

        // Validate mã số thiết bị độc nhất
        if (equipmentService.getEquipmentByCode(equipment.getCode()).isPresent()) {
            result.rejectValue("code", "error.equipment", "Mã số thiết bị đã tồn tại");
            model.addAttribute("categories", categoryService.getAllCategories());
            return "admin/equipment/form";
        }

        equipmentService.saveEquipment(equipment);
        return "redirect:/admin/equipment";
    }

    @GetMapping("/edit/{id}")
    public String showEditEquipmentForm(@PathVariable Long id, Model model) {
        Equipment equipment = equipmentService.getEquipmentById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thiết bị"));
        model.addAttribute("equipment", equipment);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/equipment/form";
    }

    @PostMapping("/edit/{id}")
    public String updateEquipment(@PathVariable Long id, @Valid @ModelAttribute Equipment equipment, 
                                  BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "admin/equipment/form";
        }
        equipmentService.updateEquipment(id, equipment);
        return "redirect:/admin/equipment";
    }

    @GetMapping("/delete/{id}")
    public String deleteEquipment(@PathVariable Long id) {
        equipmentService.deleteEquipment(id);
        return "redirect:/admin/equipment";
    }
}
