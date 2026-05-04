package com.example.archtst.service.impl;

import com.example.archtst.enums.AddressType;
import com.example.archtst.exception.DuplicateResourceException;
import com.example.archtst.exception.ResourceNotFoundException;
import com.example.archtst.model.AddressModel;
import com.example.archtst.model.UserModel;
import com.example.archtst.persistence.repository.AddressPersistenceService;
import com.example.archtst.service.AddressService;
import com.example.archtst.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    @Autowired
    private UserService userService;
    private final AddressPersistenceService addressPersistenceService;

    public Page<AddressModel> getAddressesByUserId(UUID userId, Pageable pageable) {
        return addressPersistenceService.findAllByUserId(userId, pageable);
    }

    @Override
    public AddressModel getAddressById(UUID addressId) {
        return addressPersistenceService.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Адрес с id: " + addressId + " не найден"));
    }

    @Override
    public AddressModel prepareAddressForSaving(UserModel user, AddressModel incomingAddress) {
        if (incomingAddress.getType() == AddressType.HOME) {
            var existingHome = user.getAddresses().stream()
                    .filter(addr -> addr.getType() == AddressType.HOME)
                    .findFirst();

            if (existingHome.isPresent()) {
                AddressModel existing = existingHome.get();
                existing.setCity(incomingAddress.getCity());
                existing.setStreet(incomingAddress.getStreet());
                existing.setHouseNumber(incomingAddress.getHouseNumber());
                return existing;
            }
        } else {
            boolean isDuplicate = user.getAddresses().stream()
                    .anyMatch(a -> a.getCity().equalsIgnoreCase(incomingAddress.getCity()) &&
                            a.getStreet().equalsIgnoreCase(incomingAddress.getStreet()) &&
                            a.getHouseNumber().equalsIgnoreCase(incomingAddress.getHouseNumber()));
            if (isDuplicate) {
                throw new DuplicateResourceException("Такой физический адрес уже добавлен этому пользователю");
            }
        }

        incomingAddress.setUser(user);
        return incomingAddress;
    }

    @Override
    @Transactional
    public AddressModel saveAddress(AddressModel address) {
        return addressPersistenceService.save(address);
    }

    @Override
    @Transactional
    public void deleteAddress(AddressModel address) {
        var entity = addressPersistenceService.findEntityById(address.getId());
        addressPersistenceService.delete(entity);
    }

    @Override
    @Transactional
    public AddressModel addAddressToUser(UUID userId, AddressModel incomingAddress) {
        UserModel user = userService.getUserById(userId);
        AddressModel addressToSave = prepareAddressForSaving(user, incomingAddress);
        boolean isCreated = (addressToSave.getId() == null);
        AddressModel savedAddress = saveAddress(addressToSave);
        if (isCreated) {
            user.getAddresses().add(savedAddress);
        }
        return savedAddress;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AddressModel> getUserAddresses(UUID userId, Pageable pageable) {
        userService.getUserById(userId);
        return getAddressesByUserId(userId, pageable);
    }

    @Override
    @Transactional
    public void removeAddress(UUID userId, UUID addressId) {
        UserModel user = userService.getUserById(userId);
        AddressModel address = getAddressById(addressId);
        if (!address.getUser().getId().equals(user.getId())) {
            throw new com.example.archtst.exception.InvalidAddressOwnerException("Этот адрес не принадлежит данному пользователю");
        }
        deleteAddress(address);
    }
}
