package com.skincare.ecommerce.mapper;

import com.skincare.ecommerce.dto.request.CategoryRequest;
import com.skincare.ecommerce.dto.response.CategoryResponse;
import com.skincare.ecommerce.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequest request) {
        if (request == null) {
            return null;
        }
        return Category.builder()
                .name(request.getName().trim())
                .description(request.getDescription())
                .build();
    }

    public CategoryResponse toResponse(Category category) {
        if (category == null) {
            return null;
        }
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }

    public void updateEntityFromRequest(CategoryRequest request, Category category) {
        if (request == null || category == null) {
            return;
        }
        category.setName(request.getName().trim());
        category.setDescription(request.getDescription());
    }
}
