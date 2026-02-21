package com.example.ims_backend.services;

import com.example.ims_backend.dto.ProductDto;
import com.example.ims_backend.dto.Response;
import com.example.ims_backend.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductService {

    ProductDto saveProduct(ProductDto productDto, MultipartFile imageFile);

    ProductDto updateProduct(ProductDto productDto, MultipartFile imageFile);

    List<ProductDto> getAllProducts();

    ProductDto getProductById(Long id);

    void deleteId(Long id);

    List<ProductDto> searchProduct(String input);

}
