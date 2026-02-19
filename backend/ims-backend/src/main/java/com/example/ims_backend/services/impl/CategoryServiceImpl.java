package com.example.ims_backend.services.impl;

import com.example.ims_backend.dto.CategoryDto;
import com.example.ims_backend.dto.Response;
import com.example.ims_backend.repository.CategoryRepository;
import com.example.ims_backend.services.ICategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements ICategoryService {
    private final CategoryRepository categoryRepository;


    /**
     * @param categoryDto
     * @return
     */
    @Override
    public Response createCategory(CategoryDto categoryDto) {
        return null;
    }

    /**
     * @return
     */
    @Override
    public Response getAllCategories() {
        return null;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Response getCategoryById(Long id) {
        return null;
    }

    /**
     * @param id
     * @param categoryDto
     * @return
     */
    @Override
    public Response updateCategory(Long id, CategoryDto categoryDto) {
        return null;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Response deleteCategory(Long id) {
        return null;
    }
}
