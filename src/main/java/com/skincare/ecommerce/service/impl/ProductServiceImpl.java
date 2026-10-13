package com.skincare.ecommerce.service.impl;

import com.skincare.ecommerce.dto.request.ProductCreateRequest;
import com.skincare.ecommerce.dto.request.ProductFilterRequest;
import com.skincare.ecommerce.dto.request.ProductUpdateRequest;
import com.skincare.ecommerce.dto.response.ImageUploadResponse;
import com.skincare.ecommerce.dto.response.PageResponse;
import com.skincare.ecommerce.dto.response.ProductResponse;
import com.skincare.ecommerce.entity.Category;
import com.skincare.ecommerce.entity.Product;
import com.skincare.ecommerce.exception.ResourceNotFoundException;
import com.skincare.ecommerce.mapper.ProductMapper;
import com.skincare.ecommerce.repository.CategoryRepository;
import com.skincare.ecommerce.repository.ProductRepository;
import com.skincare.ecommerce.repository.specification.ProductSpecification;
import com.skincare.ecommerce.service.FileStorageService;
import com.skincare.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("price", "name", "createdAt", "stock");

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final FileStorageService fileStorageService;

    @Override
    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {
        log.info("Creating product: {}", request.getName());
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + request.getCategoryId()));

        Product product = productMapper.toEntity(request, category);
        Product savedProduct = productRepository.save(product);
        return productMapper.toResponse(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
        return productMapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> getAllProducts(int page, int size, String sortBy, String sortDir) {
        Pageable pageable = createPageable(page, size, sortBy, sortDir);
        Page<Product> productPage = productRepository.findAll(pageable);
        return PageResponse.from(productPage.map(productMapper::toResponse));
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductUpdateRequest request) {
        log.info("Updating product with ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + request.getCategoryId()));

        productMapper.updateEntityFromRequest(request, product, category);
        Product updatedProduct = productRepository.save(product);
        return productMapper.toResponse(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        log.info("Deleting product with ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));

        if (product.getImageUrl() != null) {
            fileStorageService.deleteFile(product.getImageUrl());
        }

        productRepository.delete(product);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> filterProducts(ProductFilterRequest filterRequest) {
        Specification<Product> spec = ProductSpecification.withFilters(filterRequest);
        Pageable pageable = createPageable(
                filterRequest.getPage(),
                filterRequest.getSize(),
                filterRequest.getSortBy(),
                filterRequest.getSortDir()
        );

        Page<Product> productPage = productRepository.findAll(spec, pageable);
        return PageResponse.from(productPage.map(productMapper::toResponse));
    }

    @Override
    @Transactional
    public ImageUploadResponse uploadProductImage(Long id, MultipartFile file) {
        log.info("Uploading image for product ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));

        // Delete existing image if one exists
        if (product.getImageUrl() != null) {
            fileStorageService.deleteFile(product.getImageUrl());
        }

        String storedImageUrl = fileStorageService.storeFile(file);
        product.setImageUrl(storedImageUrl);
        productRepository.save(product);

        return ImageUploadResponse.builder()
                .productId(product.getId())
                .imageUrl(storedImageUrl)
                .message("Product image uploaded successfully")
                .build();
    }

    private Pageable createPageable(int page, int size, String sortBy, String sortDir) {
        String validatedSortBy = (sortBy != null && ALLOWED_SORT_FIELDS.contains(sortBy)) ? sortBy : "createdAt";
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDir) ? Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(Math.max(page, 0), Math.max(size, 1), Sort.by(direction, validatedSortBy));
    }
}
