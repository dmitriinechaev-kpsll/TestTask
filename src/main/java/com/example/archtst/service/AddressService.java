package com.example.archtst.service;

import com.example.archtst.entity.Address;
import com.example.archtst.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AddressService {
    Page<Address> getAddressesByUserId(UUID userId, Pageable pageable);
    Address getAddressById(UUID addressId);
    void deleteAddress(Address address);
    Address prepareAddressForSaving(User user, Address incomingAddress);
    Address saveAddress(Address address);
}
