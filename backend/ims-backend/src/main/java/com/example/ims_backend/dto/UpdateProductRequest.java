package com.example.ims_backend.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UpdateProductRequest {
    private MultipartFile imageFile;

    private Long productId;

    private Long categoryId;

    private String name;

    private String sku;

    private BigDecimal price;

    private Integer stockQuantity;

    private String description;


}