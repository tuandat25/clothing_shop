package com.tuandat.clothingshop.services.category;

import com.tuandat.clothingshop.dtos.CategoryDTO;
import com.tuandat.clothingshop.models.Category;
import com.tuandat.clothingshop.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{
    private final CategoryRepository categoryRepository;

    @Override
    public Category createCategory(CategoryDTO dto) {
        Category category = Category.builder().name(dto.getName()).build();
        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(long id) {
        return categoryRepository.findById(id).orElseThrow(()-> new RuntimeException("Category not found"));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category updateCategory(long categoryId, CategoryDTO category) {
        Category existingCategory= getCategoryById(categoryId);
        existingCategory.setName(category.getName());
        return categoryRepository.save(existingCategory);
    }

    @Override
    public Category deleteCategory(long id) throws Exception {
        categoryRepository.deleteById(id);
        return null;
    }
}
