package com.skincare.ecommerce.dto.response;

import com.skincare.ecommerce.entity.SkinType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Product response data")
public class ProductResponse {

    @Schema(description = "Product ID", example = "1")
    private Long id;

    @Schema(description = "Product name", example = "Niacinamide 10% + Zinc 1%")
    private String name;

    @Schema(description = "Product description", example = "High-strength vitamin and mineral blemish formula")
    private String description;

    @Schema(description = "Brand name", example = "The Ordinary")
    private String brand;

    @Schema(description = "Product price", example = "6.50")
    private BigDecimal price;

    @Schema(description = "Available inventory stock", example = "100")
    private Integer stock;

    @Schema(description = "Target skin type", example = "COMBINATION")
    private SkinType skinType;

    @Schema(description = "Product ingredients", example = "Aqua, Niacinamide, Zinc PCA...")
    private String ingredients;

    @Schema(description = "Product volume/size", example = "30ml")
    private String volume;

    @Schema(description = "URL path to product image", example = "/uploads/a3f2b1c4-niacinamide.png")
    private String imageUrl;

    @Schema(description = "Whether the product is a bestseller", example = "true")
    private Boolean isBestSeller;

    @Schema(description = "Whether the product is a new arrival", example = "false")
    private Boolean isNew;

    @Schema(description = "Category of the product")
    private CategoryResponse category;

    @Schema(description = "Timestamp when the product was created")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the product was last updated")
    private LocalDateTime updatedAt;
}
