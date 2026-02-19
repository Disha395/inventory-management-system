package com.example.ims_backend.services.impl;

import com.example.ims_backend.dto.CategoryDto;
import com.example.ims_backend.entity.Category;
import com.example.ims_backend.exceptions.NotFoundException;
import com.example.ims_backend.repository.CategoryRepository;
import com.example.ims_backend.services.ICategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CategoryServiceImpl implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {

        Category category = modelMapper.map(categoryDto, Category.class);

        Category savedCategory = categoryRepository.save(category);

        log.info("Category created with ID: {}", savedCategory.getId());

        return modelMapper.map(savedCategory, CategoryDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories() {

        List<Category> categories = categoryRepository
                .findAll(Sort.by(Sort.Direction.DESC, "id"));

        return modelMapper.map(
                categories,
                new TypeToken<List<CategoryDto>>() {}.getType()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category Not Found with id: " + id));

        return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {

        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category Not Found with id: " + id));

        modelMapper.map(categoryDto, existingCategory);

        Category updatedCategory = categoryRepository.save(existingCategory);

        log.info("Category updated with ID: {}", updatedCategory.getId());

        return modelMapper.map(updatedCategory, CategoryDto.class);
    }

    @Override
    public void deleteCategory(Long id) {

        if (!categoryRepository.existsById(id)) {
            throw new NotFoundException("Category Not Found with id: " + id);
        }

        categoryRepository.deleteById(id);

        log.info("Category deleted with ID: {}", id);
    }
}
