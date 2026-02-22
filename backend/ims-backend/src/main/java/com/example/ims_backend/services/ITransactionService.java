package com.example.ims_backend.services;

import com.example.ims_backend.dto.Response;
import com.example.ims_backend.dto.TransactionDto;
import com.example.ims_backend.dto.TransactionRequest;
import com.example.ims_backend.enums.TransactionStatus;

public interface ITransactionService {
    TransactionDto purchase(TransactionRequest transactionRequest);

    TransactionDto sell(TransactionRequest transactionRequest);

    TransactionDto returnToSupplier(TransactionRequest transactionRequest);

    Response getAllTransactions(int page, int size, String filter);

    Response getAllTransactionById(int month, int year);

    Response getAllTransactionByMonthAndYear(int month, int year);

    Response updateTransactionStatus(Long transactionId, TransactionStatus status);
}
