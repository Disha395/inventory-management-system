package com.example.ims_backend.controller;

import com.example.ims_backend.dto.Response;
import com.example.ims_backend.dto.TransactionDto;
import com.example.ims_backend.dto.TransactionRequest;
import com.example.ims_backend.enums.TransactionStatus;
import com.example.ims_backend.services.ITransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {


    private final ITransactionService transactionService;

    @PostMapping("/purchase")
    public ResponseEntity<TransactionDto> purchaseInventory(@RequestBody @Valid TransactionRequest transactionRequest) {
        return ResponseEntity.ok(transactionService.purchase(transactionRequest));
    }

    @PostMapping("/sell")
    public ResponseEntity<TransactionDto> makeSale(@RequestBody @Valid TransactionRequest transactionRequest) {
        return ResponseEntity.ok(transactionService.sell(transactionRequest));
    }

    @PostMapping("/return")
    public ResponseEntity<TransactionDto> returnToSupplier(@RequestBody @Valid TransactionRequest transactionRequest) {
        return ResponseEntity.ok(transactionService.returnToSupplier(transactionRequest));
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000") int size,
            @RequestParam(required = false) String filter) {

        System.out.println("SEARCH VALUE IS: " +filter);
        return ResponseEntity.ok(transactionService.getAllTransactions(page, size, filter));
    }


    @GetMapping("/{id}")
    public ResponseEntity<Response> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getAllTransactionById(id));
    }

    @GetMapping("/by-month-year")
    public ResponseEntity<Response> getTransactionByMonthAndYear(
            @RequestParam int month,
            @RequestParam int year) {

        return ResponseEntity.ok(transactionService.getAllTransactionByMonthAndYear(month, year));
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<Response> updateTransactionStatus(
            @PathVariable Long transactionId,
            @RequestBody TransactionStatus status) {

        return ResponseEntity.ok(transactionService.updateTransactionStatus(transactionId, status));
    }


}