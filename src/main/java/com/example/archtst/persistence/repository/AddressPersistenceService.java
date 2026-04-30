package com.example.archtst.persistence.repository;

import com.example.archtst.model.AddressModel;
import com.example.archtst.persistence.entity.Address;
import com.example.archtst.persistence.mapper.AddressEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AddressPersistenceService {

    private final AddressRepository addressRepository;
    private final AddressEntityMapper mapper;

    public AddressModel save(AddressModel model) {
        Address entity = mapper.toEntity(model);
        return mapper.toModel(addressRepository.save(entity));
    }

    public Optional<AddressModel> findById(UUID id) {
        return addressRepository.findById(id).map(mapper::toModel);
    }

    public Page<AddressModel> findAllByUserId(UUID userId, Pageable pageable) {
        return addressRepository.findAllByUserId(userId, pageable).map(mapper::toModel);
    }

    public void delete(Address entity) {
        addressRepository.delete(entity);
    }

    public Address findEntityById(UUID id) {
        return addressRepository.findById(id).orElse(null);
    }
}