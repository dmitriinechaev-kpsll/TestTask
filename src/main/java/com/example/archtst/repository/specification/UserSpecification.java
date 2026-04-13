package com.example.archtst.repository.specification;

import com.example.archtst.dto.UserSearchCriteria;
import com.example.archtst.entity.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    public static Specification<User> byCriteria(UserSearchCriteria criteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Email filter (exact match)
            emailCondition(criteria, root, criteriaBuilder, predicates);

            // 2. Name filter (partial match, without register)
            nameCondition(criteria, root, criteriaBuilder, predicates);

            // 3. Age filter (from ... to ...)
            ageCondition(criteria, root, criteriaBuilder, predicates);

            // Gathering all predicates with AND
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static void ageCondition(UserSearchCriteria criteria, Root<User> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
        // 1. Checking minimal age (from ...)
        if (criteria.minAge() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(User.Fields.age), criteria.minAge()));
        }

        // 2. Checking maximum age (up ...)
        if (criteria.maxAge() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(User.Fields.age), criteria.maxAge()));
        }
    }

    private static void emailCondition(UserSearchCriteria criteria, Root<User> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
        if (criteria.email() != null && !criteria.email().isBlank()) {
            predicates.add(criteriaBuilder.equal(root.get(User.Fields.email), criteria.email()));
        }
    }

    private static void nameCondition(UserSearchCriteria criteria, Root<User> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
        if (criteria.names()!=null && !criteria.names().isEmpty()) {
            List<Predicate> namePredicates = new ArrayList<>();
            for (String name : criteria.names()) {
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