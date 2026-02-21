package com.example.ims_backend.specification;

import com.example.ims_backend.entity.Transaction;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TransactionFilter {

    public static Specification<Transaction> byFilter(String searchValue) {
        return (root, query, criteriaBuilder) -> {

            if (searchValue == null || searchValue.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            query.distinct(true); // Prevent duplicate results due to joins

            String searchPattern = "%" + searchValue.trim().toLowerCase() + "%";

            List<Predicate> predicates = new ArrayList<>();

            // ===== Base Fields =====
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(criteriaBuilder.coalesce(root.get("description"), "")),
                    searchPattern));

            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(criteriaBuilder.coalesce(root.get("note"), "")),
                    searchPattern));

            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("status").as(String.class)),
                    searchPattern));

            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("transactionType").as(String.class)),
                    searchPattern));

            // ===== Joins (created once and reused) =====
            Join<Object, Object> userJoin = root.join("user", JoinType.LEFT);
            Join<Object, Object> supplierJoin = root.join("supplier", JoinType.LEFT);
            Join<Object, Object> productJoin = root.join("product", JoinType.LEFT);
            Join<Object, Object> categoryJoin = productJoin.join("category", JoinType.LEFT);

            // ===== User Fields =====
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(criteriaBuilder.coalesce(userJoin.get("name"), "")),
                    searchPattern));

            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(criteriaBuilder.coalesce(userJoin.get("email"), "")),
                    searchPattern));

            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(criteriaBuilder.coalesce(userJoin.get("phoneNumber"), "")),
                    searchPattern));

            // ===== Supplier Fields =====
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(criteriaBuilder.coalesce(supplierJoin.get("name"), "")),
                    searchPattern));

            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(criteriaBuilder.coalesce(supplierJoin.get("contactInfo"), "")),
                    searchPattern));

            // ===== Product Fields =====
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(criteriaBuilder.coalesce(productJoin.get("name"), "")),
                    searchPattern));

            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(criteriaBuilder.coalesce(productJoin.get("sku"), "")),
                    searchPattern));

            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(criteriaBuilder.coalesce(productJoin.get("description"), "")),
                    searchPattern));

            // ===== Category Field =====
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(criteriaBuilder.coalesce(categoryJoin.get("name"), "")),
                    searchPattern));

            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }


    // ===== Filter by Month and Year =====
    public static Specification<Transaction> byMonthAndYear(int month, int year) {
        return (root, query, criteriaBuilder) -> {

            Expression<Integer> monthExpression =
                    criteriaBuilder.function("month", Integer.class, root.get("createdAt"));

            Expression<Integer> yearExpression =
                    criteriaBuilder.function("year", Integer.class, root.get("createdAt"));

            Predicate monthPredicate = criteriaBuilder.equal(monthExpression, month);
            Predicate yearPredicate = criteriaBuilder.equal(yearExpression, year);

            return criteriaBuilder.and(monthPredicate, yearPredicate);
        };
    }
}