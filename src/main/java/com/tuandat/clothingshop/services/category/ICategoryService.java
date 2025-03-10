package com.tuandat.clothingshop.services.category;

import com.tuandat.clothingshop.dtos.CategoryDTO;
import com.tuandat.clothingshop.models.Category;

import java.util.List;

public interface ICategoryService {
    Category createCategory(CategoryDTO category);
    Category getCategoryById(long id);
    List<Category> getAllCategories();
    Category updateCategory(long categoryId, CategoryDTO category);
    Category deleteCategory(long id) throws Exception;
}
