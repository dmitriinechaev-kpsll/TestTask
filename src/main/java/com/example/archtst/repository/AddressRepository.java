package com.example.archtst.repository;

import com.example.archtst.entity.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> {
    // Можно добавить метод поиска всех адресов конкретного юзера
    Page<Address> findAllByUserId(UUID userId, Pageable pageable);
}
