package com.example.archtst.facade;

import com.example.archtst.entity.Address;
import com.example.archtst.entity.User;
import com.example.archtst.service.AddressService;
import com.example.archtst.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserAddressFacade {

    private final UserService userService;
    private final AddressService addressService;

    public record AddressOperationResult(Address address, boolean isCreated) {}

    @Transactional
    public AddressOperationResult addAddressToUser(UUID userId, Address incomingAddress) {
        User user = userService.getUserById(userId);
        Address addressToSave = addressService.prepareAddressForSaving(user, incomingAddress);
        boolean isCreated = (addressToSave.getId() == null);
        Address savedAddress = addressService.saveAddress(addressToSave);
        if (isCreated) {
            user.getAddresses().add(savedAddress);
        }
        return new AddressOperationResult(savedAddress, isCreated);
    }

    @Transactional(readOnly = true)
    public Page<Address> getUserAddresses(UUID userId, Pageable pageable) {
        userService.getUserById(userId);
        return addressService.getAddressesByUserId(userId, pageable);
    }

    @Transactional
    public void removeAddress(UUID userId, UUID addressId) {
        User user = userService.getUserById(userId);
        Address address = addressService.getAddressById(addressId);
        if (!address.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Этот адрес не принадлежит данному пользователю");
        }
        addressService.deleteAddress(address);
    }
}
