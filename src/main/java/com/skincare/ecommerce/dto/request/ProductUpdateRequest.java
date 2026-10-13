package com.skincare.ecommerce.dto.request;

import com.skincare.ecommerce.entity.SkinType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request payload for updating an existing Product")
public class ProductUpdateRequest {

    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 200, message = "Product name must be between 2 and 200 characters")
    @Schema(description = "Product name", example = "Niacinamide 10% + Zinc 1% (Updated Formula)")
    private String name;

    @Schema(description = "Product description", example = "High-strength vitamin and mineral blemish formula")
    private String description;

    @NotBlank(message = "Brand is required")
    @Size(max = 100, message = "Brand must not exceed 100 characters")
    @Schema(description = "Brand name", example = "The Ordinary")
    private String brand;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "Price must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Price format must have up to 8 digits and 2 decimal places")
    @Schema(description = "Product price", example = "7.20")
    private BigDecimal price;

    @NotNull(message = "Stock is required")
    @Min(value = 0, message = "Stock must be greater than or equal to 0")
    @Schema(description = "Available inventory stock", example = "85")
    private Integer stock;

    @NotNull(message = "Skin type is required")
    @Schema(description = "Target skin type", example = "COMBINATION")
    private SkinType skinType;

    @Schema(description = "Product ingredients", example = "Aqua, Niacinamide, Zinc PCA, Dimethyl Isosorbide, Tamarindus Indica Seed Gum...")
    private String ingredients;

    @Size(max = 50, message = "Volume must not exceed 50 characters")
    @Schema(description = "Product volume/size", example = "30ml")
    private String volume;

    @Schema(description = "Flag indicating if the product is a bestseller", example = "true")
    private Boolean isBestSeller;

    @Schema(description = "Flag indicating if the product is a new arrival", example = "false")
    private Boolean isNew;

    @NotNull(message = "Category ID is required")
    @Schema(description = "ID of the associated category", example = "1")
    private Long categoryId;
}
