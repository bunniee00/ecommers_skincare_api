package com.skincare.ecommerce.service;

import com.skincare.ecommerce.dto.request.ProductCreateRequest;
import com.skincare.ecommerce.dto.request.ProductFilterRequest;
import com.skincare.ecommerce.dto.request.ProductUpdateRequest;
import com.skincare.ecommerce.dto.response.ImageUploadResponse;
import com.skincare.ecommerce.dto.response.PageResponse;
import com.skincare.ecommerce.dto.response.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

    ProductResponse createProduct(ProductCreateRequest request);

    ProductResponse getProductById(Long id);

    PageResponse<ProductResponse> getAllProducts(int page, int size, String sortBy, String sortDir);

    ProductResponse updateProduct(Long id, ProductUpdateRequest request);

    void deleteProduct(Long id);

    PageResponse<ProductResponse> filterProducts(ProductFilterRequest filterRequest);

    ImageUploadResponse uploadProductImage(Long id, MultipartFile file);
}
