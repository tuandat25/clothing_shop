package com.tuandat.clothingshop.services.category;

import com.tuandat.clothingshop.dtos.CategoryDTO;
import com.tuandat.clothingshop.models.Category;
import com.tuandat.clothingshop.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

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
    public Category getCategoryById(UUID id) {
        return categoryRepository.findById(id).orElseThrow(()-> new RuntimeException("Category not found"));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category updateCategory(UUID categoryId, CategoryDTO category) {
        Category existingCategory= getCategoryById(categoryId);
        existingCategory.setName(category.getName());
        return categoryRepository.save(existingCategory);
    }

    @Override
    public Category deleteCategory(UUID id) throws Exception {
        categoryRepository.deleteById(id);
        return null;
    }

    @Override
    public Long count() {
        return categoryRepository.count();
    }

    @Override
    public UUID getCategoryIdByName(String name) {
        return categoryRepository.findIdByName(name);
    }
}
