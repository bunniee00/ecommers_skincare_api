package com.skincare.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Standardized error response")
public class ErrorResponse {

    @Schema(description = "Timestamp when error occurred", example = "2026-10-13T14:30:00")
    private LocalDateTime timestamp;

    @Schema(description = "HTTP status code", example = "400")
    private int status;

    @Schema(description = "HTTP error title", example = "Bad Request")
    private String error;

    @Schema(description = "Human-readable error message", example = "Validation failed for request parameters")
    private String message;

    @Schema(description = "Target API path", example = "/api/products")
    private String path;

    @Schema(description = "Validation field errors (if applicable)", example = "{\"price\": \"Price must be greater than 0\"}")
    private Map<String, String> validationErrors;
}
