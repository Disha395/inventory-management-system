package com.example.ims_backend.services;

import com.example.ims_backend.dto.ProductDto;
import com.example.ims_backend.dto.Response;
import org.springframework.web.multipart.MultipartFile;

public interface IProductService {

    Response saveProduct(ProductDto productDto, MultipartFile imageFile);

    Response updateProduct(ProductDto productDto, MultipartFile imageFile);

    Response getAllProducts();

    Response getProductById(Long id);

    Response deleteId(Long id);

    Response searchProduct(String input);

}
