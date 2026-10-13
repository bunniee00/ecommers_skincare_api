package com.skincare.ecommerce.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response returned after successful image upload")
public class ImageUploadResponse {

    @Schema(description = "Product ID", example = "1")
    private Long productId;

    @Schema(description = "Public URL path to the uploaded image", example = "/uploads/3f6e1f02-a1b2-4c3d-e4f5-6789abcdef01.png")
    private String imageUrl;

    @Schema(description = "Upload confirmation message", example = "Image uploaded successfully")
    private String message;
}
