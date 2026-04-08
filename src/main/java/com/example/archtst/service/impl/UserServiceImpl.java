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
        //log.info("Поиск пользователя по ID: {}", id);
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();//userRepository.findById(id);
        }
        throw new UserNotFoundException("id: " + id);
    }

    @Override
    public boolean deleteUser(UUID id) {
        // 1. Достаем пользователя (метод уже кидает ошибку, если его нет)
        User user = getUserById(id);

        // 2. Очищаем все роли!
        // Hibernate увидит это и сделает настоящий DELETE FROM user_roles WHERE user_id = ?
        user.getUserRoles().clear();

        // 3. Вызываем "удаление" юзера
        // Hibernate сделает UPDATE users SET deleted = true WHERE id = ?
        userRepository.delete(user);

        log.info("Пользователь {} и его связи с ролями успешно удалены", user.getEmail());
        return true;
    }

    @Override
    public long getTotalUsersCount() {
        return userRepository.count();
    }

    @Override
    @Transactional // Важно! Вся операция должна быть атомарной
    public AddressUpsertResult addAddressToUser(UUID userId, AddressRequestDTO request) {
        // 1. Ищем пользователя. Если нет - ошибка 404
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("id: " + userId));

        Address addressToSave = null;
        boolean isCreated = false; // Флаг для контроллера

        // 2. Если добавляют HOME адрес, ищем, нет ли уже такого у юзера
        if (request.getType() == AddressType.HOME) {
            addressToSave = user.getAddresses().stream()
                    .filter(addr -> addr.getType() == AddressType.HOME)
                    .findFirst()
                    .orElse(null); // Вернет null, если адреса еще нет
        } else {
            boolean isPhysicalDuplicate = user.getAddresses().stream()
                    .anyMatch(a -> a.getCity().equalsIgnoreCase(request.getCity()) &&
                            a.getStreet().equalsIgnoreCase(request.getStreet()) &&
                            a.getHouseNumber().equalsIgnoreCase(request.getHouseNumber()));

            if (isPhysicalDuplicate) {
                throw new DuplicateResourceException("Такой физический адрес уже добавлен этому пользователю");
            }
        }

        // 3. Выбираем стратегию: ОБНОВЛЕНИЕ или СОЗДАНИЕ
        if (addressToSave != null) {
            // ОБНОВЛЕНИЕ (Перезаписываем поля существующего адреса)
            // Мы не трогаем addressToSave.getId() и addressToSave.getUser() - они остаются прежними
            addressToSave.setCity(request.getCity());
            addressToSave.setStreet(request.getStreet());
            addressToSave.setHouseNumber(request.getHouseNumber());
            isCreated = false; // Явно указываем, что это обновление
            // *Если в будущем добавишь другие типы (WORK),
            // type тут менять не надо, он и так HOME
        } else {
            // СОЗДАНИЕ (Маппим новый адрес из DTO)
            addressToSave = addressMapper.toEntity(request);
            addressToSave.setUser(user);
            isCreated = true; // Запоминаем, что создали новый!
        }

        // 4. Сохраняем.
        // Если у addressToSave есть ID (обновление) - он сделает SQL UPDATE.
        // Если ID нет (создание) - он сделает SQL INSERT.
        Address savedAddress = addressRepository.save(addressToSave);
        AddressResponseDTO responseDTO = addressMapper.toResponseDTO(savedAddress);

        // 5. Возвращаем ответ
        return new AddressUpsertResult(responseDTO, isCreated);
/*
        // Проверка уникальности домашнего адреса
        if (request.getType() == AddressType.HOME){
            // если домашний адрес уже есть
            boolean hasHomeAddress = user.getAddresses().stream()
                    .anyMatch(address -> address.getType().equals(AddressType.HOME));
            if (hasHomeAddress){
                throw new UserAlreadyExistException("email: " + user.getEmail());
            }
        }

        // DTO в Entity
        Address address = addressMapper.toEntity(request);

        // В БД в колонку user_id запишется ID пользователя
        address.setUser(user);

        // Сохраняем адрес
        Address savedAddress = addressRepository.save(address);
        return addressMapper.toResponseDTO(savedAddress);
 */
    }

    @Override
    public Page<AddressResponseDTO> getUserAddresses(UUID userId, Pageable pageable) {

        // 1. Проверяем, существует ли вообще такой юзер (чтобы не вернуть пустую страницу для фейкового ID)
        if (!userRepository.existsById(userId)) {
            // Выбрасываем твою ошибку (например, EntityNotFoundException),
            // которую поймает GlobalExceptionHandler и вернет 404
            throw new EntityNotFoundException("Пользователь с ID " + userId + " не найден");
        }

        // 2. Достаем адреса страницами из базы
        Page<Address> addressPage = addressRepository.findAllByUserId(userId, pageable);

        // 3. Конвертируем каждый Address внутри страницы в AddressResponseDTO
        return addressPage.map(address -> addressMapper.toResponseDTO(address));
    }

    @Override
    @Transactional // Обязательно! Чтобы все изменения сохранились в базу одной транзакцией
    public void removeAddress(UUID userId, UUID addressId) {

        // 1. Находим пользователя
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с ID " + userId + " не найден"));

        // 2. Ищем нужный адрес в коллекции адресов этого пользователя
        Address addressToRemove = user.getAddresses().stream()
                .filter(address -> address.getId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Адрес с ID " + addressId + " не найден у данного пользователя"));

        // 3. Удаляем адрес из списка
        user.getAddresses().remove(addressToRemove);

        // Магия: так как у нас в сущности User над списком адресов
        // должно стоять orphanRemoval = true,
        // Hibernate автоматически удалит эту запись из таблицы адресов!
        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserResponseDTO addRoleToUser(UUID userId, RoleRequestDTO request) {
        // 1. Ищем пользователя
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден, id: " + userId));

        // 2. Ищем роль в базе.
        Role role = roleRepository.findByName(request.roleName())
                .orElseThrow(() -> new IllegalArgumentException("Роль " + request.roleName() + " не существует в системе"));

        // 3. Назначаем роль пользователю
        user.addRole(role);

        // 4. Сохраняем
        User savedUser = userRepository.save(user);

        // 5. Возвращаем обновленного пользователя
        // 👇 ИСПРАВИЛИ U на u (используем внедренный объект userMapper)
        return userMapper.userToResponseDTO(savedUser);
    }

    @Override
    @Transactional
    public UserResponseDTO removeRoleFromUser(UUID userId, String roleName) {
        // 1. Ищем пользователя
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден, id: " + userId));

        // 2. Ищем роль
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Роль " + roleName + " не существует в системе"));

        // 3. Забираем роль (тот самый метод!)
        // Метод remove() внутри Set корректно найдет нужную роль и удалит её.
        user.removeRole(role);

        // 4. Сохраняем (Hibernate сделает DELETE FROM user_roles WHERE ...)
        User savedUser = userRepository.save(user);

        // 5. Возвращаем обновленного юзера (чтобы фронтенд сразу увидел, что список ролей изменился)
        return userMapper.userToResponseDTO(savedUser);
    }
}