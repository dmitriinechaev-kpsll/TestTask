package com.example.archtst.repository.specification;

import com.example.archtst.dto.UserSearchRequestDTO; // Ваш DTO для поиска
import com.example.archtst.entity.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    // Принимаем DTO, которое приходит в POST запросе
    public static Specification<User> byCriteria(UserSearchRequestDTO request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Фильтр по Email (точное совпадение)
            if (StringUtils.hasText(request.getEmail())) {
                predicates.add(criteriaBuilder.equal(root.get("email"), request.getEmail()));
            }

            // 2. Фильтр по Имени (частичное совпадение, без регистра)
            // like lower(name) %...%
            if (StringUtils.hasText(request.getName())) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + request.getName().toLowerCase() + "%"
                ));
            }

            // 3. Фильтр по Возрасту (старше чем)
            if (request.getOlderThan() != null) {
                predicates.add(criteriaBuilder.greaterThan(root.get("age"), request.getOlderThan()));
            }

            // Собираем все предикаты через AND
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}