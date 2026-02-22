package com.example.ims_backend.services.impl;

import com.example.ims_backend.dto.Response;
import com.example.ims_backend.dto.TransactionDto;
import com.example.ims_backend.dto.TransactionRequest;
import com.example.ims_backend.entity.Product;
import com.example.ims_backend.entity.Supplier;
import com.example.ims_backend.entity.Transaction;
import com.example.ims_backend.entity.User;
import com.example.ims_backend.enums.TransactionStatus;
import com.example.ims_backend.enums.TransactionType;
import com.example.ims_backend.exceptions.NameValueRequiredException;
import com.example.ims_backend.exceptions.NotFoundException;
import com.example.ims_backend.repository.ProductRepository;
import com.example.ims_backend.repository.SupplierRepository;
import com.example.ims_backend.repository.TransactionRepository;
import com.example.ims_backend.services.ITransactionService;
import com.example.ims_backend.services.IUserService;
import com.example.ims_backend.specification.TransactionFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements ITransactionService {

    private final TransactionRepository transactionRepository;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final IUserService userService;
    private final ModelMapper modelMapper;

    /**
     * @param transactionRequest
     * @return
     */
    @Override
    @Transactional
    public TransactionDto purchase(TransactionRequest transactionRequest) {

        Long productId = transactionRequest.getProductId();
        Long supplierId = transactionRequest.getSupplierId();
        Integer quantity = transactionRequest.getQuantity();

        // ===== Validation =====
        if (productId == null)
            throw new NameValueRequiredException("Product Id is required!");

        if (supplierId == null)
            throw new NameValueRequiredException("Supplier Id is required!");

        if (quantity == null || quantity <= 0)
            throw new NameValueRequiredException("Quantity must be greater than 0");

        log.info("Processing PURCHASE - Product ID: {}, Supplier ID: {}, Quantity: {}",
                productId, supplierId, quantity);

        // ===== Fetch Product =====
        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new NotFoundException("Product not found with id: " + productId));

        // ===== Fetch Supplier =====
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() ->
                        new NotFoundException("Supplier not found with id: " + supplierId));

        // ===== Get Current User =====
        User user = userService.getCurrentLoggedInUser();

        // ===== Update Stock (JPA will auto-flush inside transaction) =====
        product.setStockQuantity(product.getStockQuantity() + quantity);

        // ===== Create Transaction =====
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.PURCHASE)
                .status(TransactionStatus.COMPLETED)
                .product(product)
                .user(user)
                .supplier(supplier)
                .totalProducts(quantity)
                .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
                .description(transactionRequest.getDescription())
                .note(transactionRequest.getNote())
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);

        log.info("Purchase completed successfully. Transaction ID: {}", savedTransaction.getId());

        return modelMapper.map(savedTransaction, TransactionDto.class);
    }


    /**
     * @param transactionRequest
     * @return
     */
    @Override
    @Transactional
    public TransactionDto sell(TransactionRequest transactionRequest) {
        Long productId = transactionRequest.getProductId();
        Integer quantity = transactionRequest.getQuantity();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found with id : " + productId));

        User user = userService.getCurrentLoggedInUser();

        //update the stock quantity and re-save
        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);

        //create a transaction
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.SALE)
                .status(TransactionStatus.COMPLETED)
                .product(product)
                .user(user)
                .totalProducts(quantity)
                .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
                .description(transactionRequest.getDescription())
                .note(transactionRequest.getNote())
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);
        return modelMapper.map(savedTransaction, TransactionDto.class);

    }

    /**
     * @param transactionRequest
     * @return
     */
    @Override
    public TransactionDto returnToSupplier(TransactionRequest transactionRequest) {
        Long productId = transactionRequest.getProductId();
        Long supplierId = transactionRequest.getSupplierId();
        Integer quantity = transactionRequest.getQuantity();

        if (supplierId == null) throw new NameValueRequiredException("Supplier Id is Required");

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product Not Found"));

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new NotFoundException("Supplier Not Found"));

        User user = userService.getCurrentLoggedInUser();

        //update the stock quantity and re-save
        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);

        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.RETURN_TO_SUPPLIER)
                .status(TransactionStatus.PROCESSING)
                .product(product)
                .user(user)
                .totalProducts(quantity)
                .totalPrice(BigDecimal.ZERO)
                .description(transactionRequest.getDescription())
                .note(transactionRequest.getNote())
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);

        return modelMapper.map(savedTransaction, TransactionDto.class);


    }

    /**
     * @param page
     * @param size
     * @param filter
     * @return
     */
    @Override
    public Response getAllTransactions(int page, int size, String filter) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        //user the Transaction specification
        Specification<Transaction> spec = TransactionFilter.byFilter(filter);
        Page<Transaction> transactionPage = transactionRepository.findAll(spec, pageable);

        List<TransactionDto> transactionDTOS = modelMapper.map(transactionPage.getContent(), new TypeToken<List<TransactionDto>>() {
        }.getType());

        transactionDTOS.forEach(transactionDTO -> {
            transactionDTO.setUser(null);
            transactionDTO.setProduct(null);
            transactionDTO.setSupplier(null);
        });

        return Response.builder()
                .status(200)
                .message("success")
                .transactions(transactionDTOS)
                .totalElements(transactionPage.getTotalElements())
                .totalPages(transactionPage.getTotalPages())
                .build();

    }

    /**
     * @param month
     * @param year
     * @return
     */
    @Override
    public Response getAllTransactionById(int month , int year) {
        List<Transaction> transactions = transactionRepository.findAll(TransactionFilter.byMonthAndYear(month, year));

        List<TransactionDto> transactionDTOS = modelMapper.map(transactions, new TypeToken<List<TransactionDto>>() {
        }.getType());

        transactionDTOS.forEach(transactionDTO -> {
            transactionDTO.setUser(null);
            transactionDTO.setProduct(null);
            transactionDTO.setSupplier(null);
        });

        return Response.builder()
                .status(200)
                .message("success")
                .transactions(transactionDTOS)
                .build();
    }

    /**
     * @param month
     * @param year
     * @return
     */
    @Override
    public Response getAllTransactionByMonthAndYear(int month, int year) {
        List<Transaction> transactions = transactionRepository.findAll(TransactionFilter.byMonthAndYear(month, year));

        List<TransactionDto> transactionDTOS = modelMapper.map(transactions, new TypeToken<List<TransactionDto>>() {
        }.getType());

        transactionDTOS.forEach(transactionDTO -> {
            transactionDTO.setUser(null);
            transactionDTO.setProduct(null);
            transactionDTO.setSupplier(null);
        });

        return Response.builder()
                .status(200)
                .message("success")
                .transactions(transactionDTOS)
                .build();    }

    /**
     * @param transactionId
     * @param status
     * @return
     */
    @Override
    public Response updateTransactionStatus(Long transactionId, TransactionStatus status) {
        Transaction existingTransaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new NotFoundException("Transaction Not Found"));

        existingTransaction.setStatus(status);
        existingTransaction.setUpdateAt(LocalDateTime.now());

        transactionRepository.save(existingTransaction);

        return Response.builder()
                .status(200)
                .message("Transaction Status Successfully Updated")
                .build();
    }
}
