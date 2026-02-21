package com.example.ims_backend.services.impl;

import com.example.ims_backend.dto.ProductDto;
import com.example.ims_backend.entity.Category;
import com.example.ims_backend.entity.Product;
import com.example.ims_backend.exceptions.NotFoundException;
import com.example.ims_backend.repository.CategoryRepository;
import com.example.ims_backend.repository.ProductRepository;
import com.example.ims_backend.services.IProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductServiceImpl implements IProductService {

    private  final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private  final CategoryRepository categoryRepository;

    private static final String IMAGE_DIRECTORY = System.getProperty("user.dir") + "/product-images/";

    private static final String IMAGE_DIRECTORY_2 = ""; //TODO : image directory to the frontend (add later)


    /**
     * @param productDto
     * @param imageFile
     * @return
     */
    @Override
    public ProductDto saveProduct(ProductDto productDto, MultipartFile imageFile) {

        Category category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found!"));

        Product productToSave = Product.builder()
                .name(productDto.getName())
                .sku(productDto.getSku())
                .price(productDto.getPrice())
                .stockQuantity(productDto.getStockQuantity())
                .description(productDto.getDescription())
                .category(category)
                .build();

        if (imageFile != null && !imageFile.isEmpty()) {
            log.info("Image file uploaded");
            String imagePath = saveImage(imageFile);
            log.info("Image saved at: {}", imagePath);
            productToSave.setImageUrl(imagePath);
        }

        Product savedProduct = productRepository.save(productToSave);
        log.info("Saved product with ID: {}", productDto.getProductId());

        return modelMapper.map(savedProduct, ProductDto.class);
    }

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    private String saveImage(MultipartFile imageFile) {

        if (imageFile == null || imageFile.isEmpty()) {
            throw new IllegalArgumentException("Image file is required");
        }

        String contentType = imageFile.getContentType();

        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }

        if (imageFile.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size must be less than 5MB");
        }

        try {
            // Create directory if not exists
            File directory = new File(IMAGE_DIRECTORY);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Sanitize filename
            String originalFileName =
                    StringUtils.cleanPath(imageFile.getOriginalFilename());

            String uniqueFileName = UUID.randomUUID() + "_" + originalFileName;

            String imagePath = Paths
                    .get(IMAGE_DIRECTORY, uniqueFileName)
                    .toString();

            File destinationFile = new File(imagePath);
            imageFile.transferTo(destinationFile);

            return imagePath;

        } catch (IOException e) {
            throw new RuntimeException("Failed to store image", e);
        }
    }



    /**
     * @param productDto
     * @param imageFile
     * @return
     */

    @Override
    public ProductDto updateProduct(ProductDto productDto, MultipartFile imageFile) {
        //check if product exists
        Product existingProduct = productRepository.findById(productDto.getProductId())
                .orElseThrow(() -> new NotFoundException("Product Not Found"));

        //check if image is there for the product to update and upload
        if(imageFile != null && !imageFile.isEmpty()){
            String imagePath = saveImage(imageFile); //TODO : change when set up frontend

            log.info("Image saved at: {}", imagePath);
            existingProduct.setImageUrl(imagePath);
        }

        //check if category is to be changed for products
        if(productDto.getCategoryId() != null && productDto.getCategoryId() > 0){
            Category category = categoryRepository.findById(productDto.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Category Not Found!"));
            existingProduct.setCategory(category);

        }

        //check if product fields is to be changed and updated
        if(productDto.getName() != null && !productDto.getName().isBlank()){
            existingProduct.setName(productDto.getName());
        }

        if(productDto.getSku() != null && !productDto.getSku().isBlank()){
            existingProduct.setSku(productDto.getSku());
        }

        if(productDto.getDescription() != null && !productDto.getDescription().isBlank()){
            existingProduct.setDescription(productDto.getDescription());
        }

        if(productDto.getPrice() != null && productDto.getPrice().compareTo(BigDecimal.ZERO) >= 0){
            existingProduct.setPrice(productDto.getPrice());
        }

        if(productDto.getStockQuantity() != null && productDto.getStockQuantity() >= 0){
            existingProduct.setStockQuantity(productDto.getStockQuantity());
        }

        //update product
        Product savedProduct = productRepository.save(existingProduct);
        log.info("Updated product with ID: {}", productDto.getProductId());
        return modelMapper.map(savedProduct, ProductDto.class);

    }

    /**
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> getAllProducts() {
        List<Product> productList = productRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<ProductDto> productDTOList = modelMapper.map(productList, new TypeToken<List<ProductDto>>() {
        }.getType());

        return productDTOList;

    }

    /**
     * @param id
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Product not found!")
        );
        return modelMapper.map(product, ProductDto.class);

    }

    /**
     * @param id
     * @return
     */
    @Override
    public void deleteId(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product Not Found!"));

        productRepository.delete(product);
        log.info("Deleted product with ID: {}", id);
    }

    /**
     * @param input
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> searchProduct(String input) {

        String searchTerm = input.trim();

        List<Product> products =
                productRepository
                        .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                                searchTerm, searchTerm
                        );

        return modelMapper.map(
                products,
                new TypeToken<List<ProductDto>>() {}.getType()
        );
    }
}
