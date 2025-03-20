package com.tuandat.clothingshop.services.category;

import com.tuandat.clothingshop.dtos.CategoryDTO;
import com.tuandat.clothingshop.models.Category;

import java.util.*;

public interface ICategoryService {
    Category createCategory(CategoryDTO category);
    Category getCategoryById(UUID id);
    List<Category> getAllCategories();
    Category updateCategory(UUID categoryId, CategoryDTO category);
    Category deleteCategory(UUID id) throws Exception;
    UUID getCategoryIdByName(String name);
    Long count();
}
