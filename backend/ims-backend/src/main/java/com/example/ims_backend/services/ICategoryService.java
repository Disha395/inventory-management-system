package com.example.ims_backend.services;

import com.example.ims_backend.dto.CategoryDto;
import com.example.ims_backend.dto.Response;

public interface ICategoryService {
    Response createCategory(CategoryDto categoryDto);

    Response getAllCategories();

    Response getCategoryById(Long id);

    Response updateCategory(Long id, CategoryDto categoryDto);

    Response deleteCategory(Long id);
}
