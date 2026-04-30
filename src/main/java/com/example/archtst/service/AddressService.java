package com.example.archtst.service;

import com.example.archtst.model.AddressModel;
import com.example.archtst.model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AddressService {
    Page<AddressModel> getAddressesByUserId(UUID userId, Pageable pageable);
    AddressModel getAddressById(UUID addressId);
    void deleteAddress(AddressModel address);
    AddressModel prepareAddressForSaving(UserModel user, AddressModel incomingAddress);
    AddressModel saveAddress(AddressModel address);

    AddressModel addAddressToUser(UUID userId, AddressModel incomingAddress);
    Page<AddressModel> getUserAddresses(UUID userId, Pageable pageable);
    void removeAddress(UUID userId, UUID addressId);
}
