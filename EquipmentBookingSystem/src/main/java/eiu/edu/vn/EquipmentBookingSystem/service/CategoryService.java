package eiu.edu.vn.EquipmentBookingSystem.service;

import eiu.edu.vn.EquipmentBookingSystem.model.Category;
import eiu.edu.vn.EquipmentBookingSystem.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    public void updateCategory(Long id, Category category) {
        Optional<Category> existingCategory = categoryRepository.findById(id);
        if (existingCategory.isPresent()) {
            Category cat = existingCategory.get();
            cat.setName(category.getName());
            cat.setDescription(category.getDescription());
            categoryRepository.save(cat);
        }
    }
}
