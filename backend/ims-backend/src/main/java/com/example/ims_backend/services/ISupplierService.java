package com.example.ims_backend.services;

import com.example.ims_backend.dto.Response;
import com.example.ims_backend.dto.SupplierDto;

import java.util.List;

public interface ISupplierService {
    SupplierDto addSupplier(SupplierDto supplierDto);

    SupplierDto updateSupplier(Long id, SupplierDto supplierDto);

    List<SupplierDto> getAllSuppliers();

    SupplierDto getSupplierById(Long id);

    void deleteSupplier(Long id);
}
