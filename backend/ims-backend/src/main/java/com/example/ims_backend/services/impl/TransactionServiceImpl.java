package com.example.ims_backend.services.impl;

import com.example.ims_backend.dto.Response;
import com.example.ims_backend.dto.TransactionRequest;
import com.example.ims_backend.enums.TransactionStatus;
import com.example.ims_backend.repository.ProductRepository;
import com.example.ims_backend.repository.SupplierRepository;
import com.example.ims_backend.repository.TransactionRepository;
import com.example.ims_backend.services.ITransactionService;
import com.example.ims_backend.services.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
    public Response purchase(TransactionRequest transactionRequest) {
        Long productId = transactionRequest.getProductId();
        Long supplierId = transactionRequest.getSupplierId();
        Integer quantity = transactionRequest.getQuantity();



    }

    /**
     * @param transactionRequest
     * @return
     */
    @Override
    public Response sell(TransactionRequest transactionRequest) {
        return null;
    }

    /**
     * @param transactionRequest
     * @return
     */
    @Override
    public Response returnToSupplier(TransactionRequest transactionRequest) {
        return null;
    }

    /**
     * @param page
     * @param size
     * @param filter
     * @return
     */
    @Override
    public Response getAllTransactions(int page, int size, String filter) {
        return null;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Response getAllTransactionById(Long id) {
        return null;
    }

    /**
     * @param month
     * @param year
     * @return
     */
    @Override
    public Response getAllTransactionByMonthAndYear(int month, int year) {
        return null;
    }

    /**
     * @param transactionId
     * @param status
     * @return
     */
    @Override
    public Response updateTransactionStatus(Long transactionId, TransactionStatus status) {
        return null;
    }
}
