package com.skincare.ecommerce.repository.specification;

import com.skincare.ecommerce.dto.request.ProductFilterRequest;
import com.skincare.ecommerce.entity.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    private ProductSpecification() {
        // Utility class
    }

    public static Specification<Product> withFilters(ProductFilterRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Keyword search across name and brand
            if (StringUtils.hasText(request.getKeyword())) {
                String searchPattern = "%" + request.getKeyword().trim().toLowerCase() + "%";
                Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchPattern);
                Predicate brandPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("brand")), searchPattern);
                predicates.add(criteriaBuilder.or(namePredicate, brandPredicate));
            }

            // Filter by category
            if (request.getCategoryId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), request.getCategoryId()));
            }

            // Filter by skin type
            if (request.getSkinType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("skinType"), request.getSkinType()));
            }

            // Filter by brand
            if (StringUtils.hasText(request.getBrand())) {
                predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("brand")),
                        request.getBrand().trim().toLowerCase()
                ));
            }

            // Filter by min price
            if (request.getMinPrice() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), request.getMinPrice()));
            }

            // Filter by max price
            if (request.getMaxPrice() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), request.getMaxPrice()));
            }

            // Filter by best seller flag
            if (request.getIsBestSeller() != null) {
                predicates.add(criteriaBuilder.equal(root.get("isBestSeller"), request.getIsBestSeller()));
            }

            // Filter by is new flag
            if (request.getIsNew() != null) {
                predicates.add(criteriaBuilder.equal(root.get("isNew"), request.getIsNew()));
            }

            // Filter by in-stock status
            if (request.getInStock() != null) {
                if (Boolean.TRUE.equals(request.getInStock())) {
                    predicates.add(criteriaBuilder.greaterThan(root.get("stock"), 0));
                } else {
                    predicates.add(criteriaBuilder.equal(root.get("stock"), 0));
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
