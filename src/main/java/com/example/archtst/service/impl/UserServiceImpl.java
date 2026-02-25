package com.example.archtst.service.impl;

import com.example.archtst.dto.AddressRequestDTO;
import com.example.archtst.dto.AddressResponseDTO;
import com.example.archtst.dto.UserSearchRequestDTO;
import com.example.archtst.entity.Address;
import com.example.archtst.entity.User;
import com.example.archtst.exception.UserAlreadyExistException;
import com.example.archtst.exception.UserNotFoundException;
import com.example.archtst.repository.AddressRepository;
import com.example.archtst.repository.UserRepository;
import com.example.archtst.repository.specification.UserSpecification;
import com.example.archtst.service.UserService;
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

    @Transactional // Важно! Вся операция должна быть атомарной
    public AddressResponseDTO addAddressToUser(UUID userId, AddressRequestDTO request) {
        // 1. Ищем пользователя. Если нет - ошибка 404
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("id: " + userId));

        // 2. Маппим DTO в Entity (в ручную или через MapStruct)
        Address address = new Address();
        address.setCity(request.city());
        address.setStreet(request.street());
        address.setHouseNumber(request.houseNumber());

        // 3. Устанавливаем связь! (Самый важный момент)
        // В БД в колонку user_id запишется ID нашего пользователя
        address.setUser(user);

        // 4. Сохраняем адрес
        // (Можно сохранять и через user.getAddresses().add(address) + userRepo.save(user),
        // но сохранять адрес напрямую эффективнее по памяти).
        Address savedAddress = addressRepository.save(address);

        // 5. Возвращаем ответ
        return new AddressResponseDTO(
                savedAddress.getId(),
                savedAddress.getCity(),
                savedAddress.getStreet(),
                savedAddress.getHouseNumber()
        );
    }

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
        //log.info("Поиск пользователя по ID: {}", id);
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();//userRepository.findById(id);
        }
        throw new UserNotFoundException("id: " + id);
    }

    /*
    @Override
    public User getUserByEmail(String email) {
        // log.info("Поиск пользователя по email: {}", email);
        //return userRepository.findByEmail(email);
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        log.info("Такой пользователь уже существует!!");
        throw new UserNotFoundException("email: " + email);
    }

    @Override
    public Page<User> searchUsersByName(String name, Pageable pageable) {
        log.info("Поиск пользователей по имени: {}", name);
        return userRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    @Override
    public Page<User> getUsersOlderThan(Integer age, Pageable pageable) {
        log.info("Поиск пользователей старше: {}", age);
        return userRepository.findByAgeGreaterThan(age, pageable);
    }*/

    @Override
    public boolean deleteUser(UUID id) {
        getUserById(id);
        userRepository.deleteById(id);
        return true;
    }

    @Override
    public long getTotalUsersCount() {
        return userRepository.count();
    }
}