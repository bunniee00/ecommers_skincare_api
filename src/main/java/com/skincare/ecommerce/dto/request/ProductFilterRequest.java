package com.skincare.ecommerce.dto.request;

import com.skincare.ecommerce.entity.SkinType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Query parameters for searching and filtering products")
public class ProductFilterRequest {

    @Schema(description = "Keyword to search across product name and brand", example = "niacinamide")
    private String keyword;

    @Schema(description = "Filter by category ID", example = "1")
    private Long categoryId;

    @Schema(description = "Filter by skin type (OILY, DRY, COMBINATION, SENSITIVE, NORMAL, ALL)", example = "COMBINATION")
    private SkinType skinType;

    @Schema(description = "Filter by brand name", example = "The Ordinary")
    private String brand;

    @Schema(description = "Minimum price filter", example = "5.00")
    private BigDecimal minPrice;

    @Schema(description = "Maximum price filter", example = "50.00")
    private BigDecimal maxPrice;

    @Schema(description = "Filter by bestseller status", example = "true")
    private Boolean isBestSeller;

    @Schema(description = "Filter by new arrival status", example = "false")
    private Boolean isNew;

    @Schema(description = "Filter by stock availability (true: stock > 0, false: stock = 0)", example = "true")
    private Boolean inStock;

    @Builder.Default
    @Schema(description = "Page number (0-indexed)", example = "0", defaultValue = "0")
    private int page = 0;

    @Builder.Default
    @Schema(description = "Page size", example = "10", defaultValue = "10")
    private int size = 10;

    @Builder.Default
    @Schema(description = "Sort property (price, name, createdAt)", example = "createdAt", defaultValue = "createdAt")
    private String sortBy = "createdAt";

    @Builder.Default
    @Schema(description = "Sort direction (asc, desc)", example = "desc", defaultValue = "desc")
    private String sortDir = "desc";
}
