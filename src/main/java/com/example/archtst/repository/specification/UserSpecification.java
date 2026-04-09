package com.example.archtst.repository.specification;

import com.example.archtst.dto.UserSearchRequestDTO; // Ваш DTO для поиска
import com.example.archtst.entity.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    // Taking DTO, from POST
    public static Specification<User> byCriteria(UserSearchRequestDTO request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Email filter (exact match)
            emailCondition(request, root, criteriaBuilder, predicates);

            // 2. Name filter (partial match, without register)
            nameCondition(request, root, criteriaBuilder, predicates);

            // 3. Age filter (older then)
            ageCondition(request, root, criteriaBuilder, predicates);

            // Gathering all predicates with AND
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static void ageCondition(UserSearchRequestDTO request, Root<User> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
        // 1. Checking minimal age (from ...)
        if (request.getMinAge() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(User.Fields.age), request.getMinAge()));
        }

        // 2. Checking maximum age (up ...)
        if (request.getMaxAge() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(User.Fields.age), request.getMaxAge()));
        }
    }

    private static void emailCondition(UserSearchRequestDTO request, Root<User> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
        if (StringUtils.hasText(request.getEmail())) {
            predicates.add(criteriaBuilder.equal(root.get(User.Fields.email), request.getEmail()));
        }
    }

    private static void nameCondition(UserSearchRequestDTO request, Root<User> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
        if (!CollectionUtils.isEmpty(request.getNames())) {
            List<Predicate> namePredicates = new ArrayList<>();
            for (String name : request.getNames()) {
                if (StringUtils.hasText(name)) {
                    namePredicates.add(criteriaBuilder.like(
                            criteriaBuilder.lower(root.get(User.Fields.name)),
                            "%" + name.toLowerCase() + "%"));
                }
            }

            if (!namePredicates.isEmpty()) {
                predicates.add(criteriaBuilder.or(namePredicates.toArray(new Predicate[0])));
            }
        }
    }
}