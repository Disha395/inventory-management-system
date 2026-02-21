package com.example.ims_backend.services.impl;

import com.example.ims_backend.dto.SupplierDto;
import com.example.ims_backend.entity.Supplier;
import com.example.ims_backend.exceptions.NotFoundException;
import com.example.ims_backend.repository.SupplierRepository;
import com.example.ims_backend.services.ISupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierServiceImpl implements ISupplierService {

    private final SupplierRepository supplierRepository;
    private final ModelMapper modelMapper;
    /**
     * @param supplierDto
     * @return
     */
    @Override
    @Transactional
    public SupplierDto addSupplier(SupplierDto supplierDto) {
        Supplier supplierToSave = modelMapper.map(supplierDto, Supplier.class);
        Supplier savedSupplier = supplierRepository.save(supplierToSave);
        log.info("Added new supplier: {}", supplierDto.getName());
        return modelMapper.map(savedSupplier, SupplierDto.class);
    }

    /**
     * @param id
     * @param supplierDto
     * @return
     */
    @Override
    @Transactional
    public SupplierDto updateSupplier(Long id, SupplierDto supplierDto) {

        log.info("Updating supplier with ID: {}", id);

        Supplier existingSupplier = supplierRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Supplier Not Found!"));

        if (supplierDto.getName() != null && !supplierDto.getName().isBlank())
            existingSupplier.setName(supplierDto.getName());

        if (supplierDto.getContactInfo() != null && !supplierDto.getContactInfo().isBlank())
            existingSupplier.setContactInfo(supplierDto.getContactInfo());

        if (supplierDto.getAddress() != null && !supplierDto.getAddress().isBlank())
            existingSupplier.setAddress(supplierDto.getAddress());

        Supplier savedSupplier = supplierRepository.save(existingSupplier);

        return modelMapper.map(savedSupplier, SupplierDto.class);
    }

    /**
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<SupplierDto> getAllSuppliers() {

        List<Supplier> suppliers = supplierRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        return modelMapper.map(suppliers, new TypeToken<List<SupplierDto>>() {
        }.getType());
    }

    /**
     * @param id
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public SupplierDto getSupplierById(Long id) {
        log.info("Fetching supplier with ID: {}", id);
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Supplier Not Found with id : " + id));

        return modelMapper.map(supplier, SupplierDto.class);
    }

    /**
     * @param id
     * @return
     */
    @Override
    @Transactional
    public void deleteSupplier(Long id) {
        log.info("Deleting supplier with ID: {}", id);
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Supplier not found with id : " + id));

        supplierRepository.delete(supplier);

    }
}
