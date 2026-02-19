package com.example.ims_backend.services;

import com.example.ims_backend.dto.Response;
import com.example.ims_backend.dto.SupplierDto;

public interface ISupplierService {
    Response addSupplier(SupplierDto supplierDto);

    Response updateSupplier(Long id, SupplierDto supplierDto);

    Response getAllSupplier();

    Response getSupplierById(Long id);

    Response deleteSupplier(Long id);
}
