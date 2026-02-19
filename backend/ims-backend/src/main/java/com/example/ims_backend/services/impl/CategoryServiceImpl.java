package com.example.ims_backend.services.impl;

import com.example.ims_backend.dto.CategoryDto;
import com.example.ims_backend.dto.Response;
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

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements ICategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;


    /**
     * @param categoryDto
     */
    @Override
    public Response createCategory(CategoryDto categoryDto) {
        Category categoryToSave = modelMapper.map(categoryDto, Category.class);
        categoryRepository.save(categoryToSave);

        return Response.builder().
                status(200)
                .message("Category saved Successfully")
                .build();

    }

    /**
     * @return
     */
    @Override
    public Response getAllCategories() {
        List<Category> categories = categoryRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        categories.forEach(category -> category.setProducts(null));

        List<CategoryDto> categoryDTOList = modelMapper.map(categories, new TypeToken<List<CategoryDto>>() {
        }.getType());

        return Response.builder()
                .status(200)
                .message("success")
                .categories(categoryDTOList)
                .build();
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Response getCategoryById(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category Not Found"));

        CategoryDto categoryDTO = modelMapper.map(category, CategoryDto.class);

        return Response.builder()
                .status(200)
                .message("success")
                .category(categoryDTO)
                .build();
    }

    /**
     * @param id
     * @param categoryDto
     * @return
     */
    @Override
    public Response updateCategory(Long id, CategoryDto categoryDto) {

        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category Not Found"));

        existingCategory.setName(categoryDto.getName());

        categoryRepository.save(existingCategory);

        return Response.builder()
                .status(200)
                .message("Category Was Successfully Updated")
                .build();

    }

    /**
     * @param id
     * @return
     */
    @Override
    public Response deleteCategory(Long id) {

        categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category Not Found"));

        categoryRepository.deleteById(id);

        return Response.builder()
                .status(200)
                .message("Category Was Successfully Deleted")
                .build();
    }
}
