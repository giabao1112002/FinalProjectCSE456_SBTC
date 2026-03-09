package eiu.edu.vn.EquipmentBookingSystem.controller;

import eiu.edu.vn.EquipmentBookingSystem.service.EquipmentService;
import eiu.edu.vn.EquipmentBookingSystem.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String home(Model model) {
        model.addAttribute("equipmentList", equipmentService.getAllEquipment());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "home";
    }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String category, 
                        @RequestParam(required = false) String keyword, 
                        Model model) {
        var equipmentList = equipmentService.getAllEquipment();

        if (category != null && !category.isEmpty()) {
            var cat = categoryService.getCategoryById(Long.parseLong(category));
            if (cat.isPresent()) {
                equipmentList = equipmentService.getEquipmentByCategory(cat.get());
            }
        }

        if (keyword != null && !keyword.isEmpty()) {
            final String searchKeyword = keyword.toLowerCase();
            equipmentList = equipmentList.stream()
                    .filter(e -> e.getName().toLowerCase().contains(searchKeyword) ||
                                e.getCode().toLowerCase().contains(searchKeyword))
                    .toList();
        }

        model.addAttribute("equipmentList", equipmentList);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("selectedCategory", category);
        model.addAttribute("searchKeyword", keyword);
        return "home";
    }
}
