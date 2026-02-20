package com.example.archtst.repository.specification;

import com.example.archtst.dto.UserSearchRequestDTO; // Ваш DTO для поиска
import com.example.archtst.entity.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
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
            nameCondition(request, root, criteriaBuilder, predicates);

            // 3. Фильтр по Возрасту (старше чем)
            if (request.getOlderThan() != null) {
                predicates.add(criteriaBuilder.greaterThan(root.get("age"), request.getOlderThan()));
            }

            // Собираем все предикаты через AND
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static void nameCondition(UserSearchRequestDTO request, Root<User> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
        if (!CollectionUtils.isEmpty(request.getNames())) {
            List<Predicate> namePredicates = new ArrayList<>();
            for (String name : request.getNames()) {
                if (StringUtils.hasText(name)) {
                    namePredicates.add(criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("name")),
                            "%" + name.toLowerCase() + "%"
                    ));
                }
            }

            if (!namePredicates.isEmpty()) {
                predicates.add(criteriaBuilder.or(namePredicates.toArray(new Predicate[0])));
            }
        }
    }
}