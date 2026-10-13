package com.skincare.ecommerce.mapper;

import com.skincare.ecommerce.dto.request.ProductCreateRequest;
import com.skincare.ecommerce.dto.request.ProductUpdateRequest;
import com.skincare.ecommerce.dto.response.ProductResponse;
import com.skincare.ecommerce.entity.Category;
import com.skincare.ecommerce.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final CategoryMapper categoryMapper;

    public Product toEntity(ProductCreateRequest request, Category category) {
        if (request == null) {
            return null;
        }
        return Product.builder()
                .name(request.getName().trim())
                .description(request.getDescription())
                .brand(request.getBrand() != null ? request.getBrand().trim() : null)
                .price(request.getPrice())
                .stock(request.getStock())
                .skinType(request.getSkinType())
                .ingredients(request.getIngredients())
                .volume(request.getVolume())
                .isBestSeller(Boolean.TRUE.equals(request.getIsBestSeller()))
                .isNew(Boolean.TRUE.equals(request.getIsNew()))
                .category(category)
                .build();
    }

    public ProductResponse toResponse(Product product) {
        if (product == null) {
            return null;
        }
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .brand(product.getBrand())
                .price(product.getPrice())
                .stock(product.getStock())
                .skinType(product.getSkinType())
                .ingredients(product.getIngredients())
                .volume(product.getVolume())
                .imageUrl(product.getImageUrl())
                .isBestSeller(product.getIsBestSeller())
                .isNew(product.getIsNew())
                .category(categoryMapper.toResponse(product.getCategory()))
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    public void updateEntityFromRequest(ProductUpdateRequest request, Product product, Category category) {
        if (request == null || product == null) {
            return;
        }
        product.setName(request.getName().trim());
        product.setDescription(request.getDescription());
        product.setBrand(request.getBrand() != null ? request.getBrand().trim() : null);
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setSkinType(request.getSkinType());
        product.setIngredients(request.getIngredients());
        product.setVolume(request.getVolume());
        if (request.getIsBestSeller() != null) {
            product.setIsBestSeller(request.getIsBestSeller());
        }
        if (request.getIsNew() != null) {
            product.setIsNew(request.getIsNew());
        }
        if (category != null) {
            product.setCategory(category);
        }
    }
}
