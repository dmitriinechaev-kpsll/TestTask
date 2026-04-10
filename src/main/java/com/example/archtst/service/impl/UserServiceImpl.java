package com.example.archtst.service.impl;

import com.example.archtst.dto.*;
import com.example.archtst.entity.Address;
import com.example.archtst.entity.Role;
import com.example.archtst.entity.User;
import com.example.archtst.enums.AddressType;
import com.example.archtst.exception.DuplicateResourceException;
import com.example.archtst.exception.UserAlreadyExistException;
import com.example.archtst.exception.UserNotFoundException;
import com.example.archtst.mapper.AddressMapper;
import com.example.archtst.mapper.UserMapper;
import com.example.archtst.repository.AddressRepository;
import com.example.archtst.repository.RoleRepository;
import com.example.archtst.repository.UserRepository;
import com.example.archtst.repository.specification.UserSpecification;
import com.example.archtst.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    @Override
    public User createUser(User newUser) {
        log.info("Создание пользователя: {}", newUser.getEmail());
        Optional<User> optionalUser = userRepository.findByEmail(newUser.getEmail());
        if (optionalUser.isPresent()) {
            log.info("Такой пользователь уже существует!!");
            throw new UserAlreadyExistException("email: " + newUser.getEmail());
        }
        return userRepository.save(newUser);
    }

     @Override
     public Page<User> getAllUsers(Pageable pageable) {
         log.info("Получение всех пользователей (страница: {}, размер: {})",
                 pageable.getPageNumber(), pageable.getPageSize());
         return userRepository.findAll(pageable); // Возвращаем Page<User>
     }

    @Override
    public Page<User> searchUsers(UserSearchRequestDTO request, Pageable pageable) {
        Specification<User> spec = UserSpecification.byCriteria(request);
        return userRepository.findAll(spec, pageable);
    }

    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow( () -> new UserNotFoundException("id: " + id));
    }

    @Override
    public boolean deleteUser(UUID id) {
        User user = getUserById(id);
        user.getUserRoles().clear();
        userRepository.delete(user);
        log.info("Пользователь {} и его связи с ролями успешно удалены", user.getEmail());
        return true;
    }

    private Address prepareAddressForSaving(User user, Address incomingAddress) {
        if (incomingAddress.getType() == AddressType.HOME) {
            // Searching existing HOME address
            Optional<Address> existingHome = user.getAddresses().stream()
                    .filter(addr -> addr.getType() == AddressType.HOME)
                    .findFirst();

            if (existingHome.isPresent()) {
                // Update
                Address existing = existingHome.get();
                existing.setCity(incomingAddress.getCity());
                existing.setStreet(incomingAddress.getStreet());
                existing.setHouseNumber(incomingAddress.getHouseNumber());
                return existing;
            }
        } else {
            // validating for WORK and others
            boolean isDuplicate = user.getAddresses().stream()
                    .anyMatch(a -> a.getCity().equalsIgnoreCase(incomingAddress.getCity()) &&
                            a.getStreet().equalsIgnoreCase(incomingAddress.getStreet()) &&
                            a.getHouseNumber().equalsIgnoreCase(incomingAddress.getHouseNumber()));
            if (isDuplicate) {
                throw new DuplicateResourceException("Такой физический адрес уже добавлен");
            }
        }

        // linking user to address
        incomingAddress.setUser(user);
        return incomingAddress;
    }

    @Override
    @Transactional
    public AddressUpsertResult addAddressToUser(UUID userId, Address incomingAddress) {
        User user = this.getUserById(userId);
        Address addressToSave = prepareAddressForSaving(user, incomingAddress);
        boolean isCreated = (addressToSave.getId() == null); // Если ID нет - значит это создание
        Address savedAddress = addressRepository.save(addressToSave);
        return new AddressUpsertResult(savedAddress, isCreated);
    }

    @Override
    public Page<AddressResponseDTO> getUserAddresses(UUID userId, Pageable pageable) {
        this.getUserById(userId); // checking existance, exeption if not
        Page<Address> addressPage = addressRepository.findAllByUserId(userId, pageable);
        return addressPage.map(addressMapper::toResponseDTO);
    }

    @Override
    @Transactional // Обязательно! Чтобы все изменения сохранились в базу одной транзакцией
    public void removeAddress(UUID userId, UUID addressId) {
        User user = getUserById(userId);
        Address addressToRemove = user.getAddresses().stream()
                .filter(address -> address.getId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Адрес с ID " + addressId + " не найден у данного пользователя"));
        user.getAddresses().remove(addressToRemove);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    /*@Override
    @Transactional
    public UserResponseDTO addRoleToUser(UUID userId, RoleRequestDTO request) {
        User user = this.getUserById(userId);
        Role role = roleRepository.findByName(request.roleName())
                .orElseThrow(() -> new IllegalArgumentException("Роль " + request.roleName() + " не существует в системе"));
        user.addRole(role);
        User savedUser = userRepository.save(user);
        return userMapper.userToResponseDTO(savedUser);
    }

    @Override
    @Transactional
    public UserResponseDTO removeRoleFromUser(UUID userId, String roleName) {
        User user = this.getUserById(userId);
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Роль " + roleName + " не существует в системе"));
        user.removeRole(role);
        User savedUser = userRepository.save(user);
        return userMapper.userToResponseDTO(savedUser);
    }*/
}