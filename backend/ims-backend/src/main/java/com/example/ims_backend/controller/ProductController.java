package com.example.ims_backend.controller;

import com.example.ims_backend.dto.*;
import com.example.ims_backend.services.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;

    /**
     * CREATE PRODUCT
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> createProduct(CreateProductRequest request) {

        ProductDto productDto = new ProductDto();
        productDto.setName(request.getName());
        productDto.setSku(request.getSku());
        productDto.setPrice(request.getPrice());
        productDto.setStockQuantity(request.getStockQuantity());
        productDto.setDescription(request.getDescription());
        productDto.setCategoryId(request.getCategoryId());

        ProductDto savedProduct =
                productService.saveProduct(productDto, request.getImageFile());

        Response response = Response.builder()
                .status(HttpStatus.CREATED.value())
                .message("Product created successfully")
                .product(savedProduct)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    /**
     * GET ALL PRODUCTS
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> getAllProducts() {

        List<ProductDto> products = productService.getAllProducts();

        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .products(products)
                .build();

        return ResponseEntity.ok(response);
    }


    /**
     * GET PRODUCT BY ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> getProductById(@PathVariable Long id) {

        ProductDto product = productService.getProductById(id);

        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .product(product)
                .build();

        return ResponseEntity.ok(response);
    }


    /**
     * UPDATE PRODUCT
     */
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> updateProduct(UpdateProductRequest request) {

        ProductDto productDto = new ProductDto();

        productDto.setProductId(request.getProductId());
        productDto.setCategoryId(request.getCategoryId());
        productDto.setName(request.getName());
        productDto.setSku(request.getSku());
        productDto.setPrice(request.getPrice());
        productDto.setStockQuantity(request.getStockQuantity());
        productDto.setDescription(request.getDescription());

        ProductDto updatedProduct =
                productService.updateProduct(productDto, request.getImageFile());

        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .message("Product updated successfully")
                .product(updatedProduct)
                .build();

        return ResponseEntity.ok(response);
    }


    /**
     * DELETE PRODUCT
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> deleteProduct(@PathVariable Long id) {

        productService.deleteProduct(id);

        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .message("Product deleted successfully")
                .build();

        return ResponseEntity.ok(response);
    }


    /**
     * SEARCH PRODUCT
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> searchProduct(@RequestParam String keyword) {

        List<ProductDto> products = productService.searchProduct(keyword);

        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .products(products)
                .build();

        return ResponseEntity.ok(response);
    }

}