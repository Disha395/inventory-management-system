package com.example.ims_backend.services;

import com.example.ims_backend.dto.Response;
import com.example.ims_backend.dto.TransactionRequest;
import com.example.ims_backend.enums.TransactionStatus;

public interface ITransactionService {
    Response purchase(TransactionRequest transactionRequest);

    Response sell(TransactionRequest transactionRequest);

    Response returnToSupplier(TransactionRequest transactionRequest);

    Response getAllTransactions(int page, int size, String filter);

    Response getAllTransactionById(Long id);

    Response getAllTransactionByMonthAndYear(int month, int year);

    Response updateTransactionStatus(Long transactionId, TransactionStatus status);
}
