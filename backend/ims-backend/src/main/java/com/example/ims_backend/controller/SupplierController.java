package com.example.ims_backend.controller;

import com.example.ims_backend.dto.SupplierDto;
import com.example.ims_backend.services.ISupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final ISupplierService supplierService;

    // Create Supplier
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SupplierDto> createSupplier(@RequestBody @Valid SupplierDto supplierDto) {

        SupplierDto createdSupplier = supplierService.addSupplier(supplierDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSupplier);
    }

    // Get all suppliers
    @GetMapping
    public ResponseEntity<List<SupplierDto>> getAllSuppliers() {

        List<SupplierDto> suppliers = supplierService.getAllSuppliers();
        return ResponseEntity.ok(suppliers);
    }

    // Get supplier by ID
    @GetMapping("/{id}")
    public ResponseEntity<SupplierDto> getSupplierById(@PathVariable Long id) {

        SupplierDto supplier = supplierService.getSupplierById(id);
        return ResponseEntity.ok(supplier);
    }

    // Update supplier
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SupplierDto> updateSupplier(
            @PathVariable Long id,
            @RequestBody @Valid SupplierDto supplierDto
    ) {

        SupplierDto updatedSupplier = supplierService.updateSupplier(id, supplierDto);
        return ResponseEntity.ok(updatedSupplier);
    }

    // Delete supplier
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteSupplier(@PathVariable Long id) {

        supplierService.deleteSupplier(id);
        return ResponseEntity.ok("Supplier deleted successfully");
    }
}