package com.example.archtst.service.impl;

import com.example.archtst.entity.Address;
import com.example.archtst.entity.User;
import com.example.archtst.enums.AddressType;
import com.example.archtst.exception.DuplicateResourceException;
import com.example.archtst.exception.ResourceNotFoundException;
import com.example.archtst.repository.AddressRepository;
import com.example.archtst.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    public Page<Address> getAddressesByUserId(UUID userId, Pageable pageable) {
        return addressRepository.findAllByUserId(userId, pageable);
    }

    public Address getAddressById(UUID addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Адрес с id: " + addressId + " не найден"));
    }

    @Override
    public Address prepareAddressForSaving(User user, Address incomingAddress) {
        if (incomingAddress.getType() == AddressType.HOME) {
            Optional<Address> existingHome = user.getAddresses().stream()
                    .filter(addr -> addr.getType() == AddressType.HOME)
                    .findFirst();

            if (existingHome.isPresent()) {
                Address existing = existingHome.get();
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
    public Address saveAddress(Address address) {
        return addressRepository.save(address);
    }

    @Transactional
    public void deleteAddress(Address address) {
        addressRepository.delete(address);
    }
}
