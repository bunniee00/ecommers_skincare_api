package com.skincare.ecommerce.service;

import com.skincare.ecommerce.dto.request.ProductCreateRequest;
import com.skincare.ecommerce.dto.request.ProductUpdateRequest;
import com.skincare.ecommerce.dto.response.CategoryResponse;
import com.skincare.ecommerce.dto.response.ImageUploadResponse;
import com.skincare.ecommerce.dto.response.ProductResponse;
import com.skincare.ecommerce.entity.Category;
import com.skincare.ecommerce.entity.Product;
import com.skincare.ecommerce.entity.SkinType;
import com.skincare.ecommerce.exception.ResourceNotFoundException;
import com.skincare.ecommerce.mapper.ProductMapper;
import com.skincare.ecommerce.repository.CategoryRepository;
import com.skincare.ecommerce.repository.ProductRepository;
import com.skincare.ecommerce.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private ProductServiceImpl productService;

    private Category sampleCategory;
    private CategoryResponse sampleCategoryResponse;
    private Product sampleProduct;
    private ProductResponse sampleProductResponse;

    @BeforeEach
    void setUp() {
        sampleCategory = Category.builder()
                .id(1L)
                .name("Serum")
                .description("Facial serums")
                .build();

        sampleCategoryResponse = CategoryResponse.builder()
                .id(1L)
                .name("Serum")
                .description("Facial serums")
                .build();

        sampleProduct = Product.builder()
                .id(1L)
                .name("Hyaluronic Acid Serum")
                .brand("The Ordinary")
                .description("Hydrating serum")
                .price(new BigDecimal("7.90"))
                .stock(50)
                .skinType(SkinType.DRY)
                .ingredients("Aqua, Sodium Hyaluronate")
                .volume("30ml")
                .imageUrl("/uploads/sample.png")
                .isBestSeller(true)
                .isNew(false)
                .category(sampleCategory)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        sampleProductResponse = ProductResponse.builder()
                .id(1L)
                .name("Hyaluronic Acid Serum")
                .brand("The Ordinary")
                .description("Hydrating serum")
                .price(new BigDecimal("7.90"))
                .stock(50)
                .skinType(SkinType.DRY)
                .ingredients("Aqua, Sodium Hyaluronate")
                .volume("30ml")
                .imageUrl("/uploads/sample.png")
                .isBestSeller(true)
                .isNew(false)
                .category(sampleCategoryResponse)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("createProduct - Successfully create product when category exists")
    void createProduct_Success() {
        ProductCreateRequest request = ProductCreateRequest.builder()
                .name("Hyaluronic Acid Serum")
                .brand("The Ordinary")
                .description("Hydrating serum")
                .price(new BigDecimal("7.90"))
                .stock(50)
                .skinType(SkinType.DRY)
                .ingredients("Aqua, Sodium Hyaluronate")
                .volume("30ml")
                .isBestSeller(true)
                .isNew(false)
                .categoryId(1L)
                .build();

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(sampleCategory));
        when(productMapper.toEntity(request, sampleCategory)).thenReturn(sampleProduct);
        when(productRepository.save(sampleProduct)).thenReturn(sampleProduct);
        when(productMapper.toResponse(sampleProduct)).thenReturn(sampleProductResponse);

        ProductResponse response = productService.createProduct(request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Hyaluronic Acid Serum");
        verify(productRepository, times(1)).save(sampleProduct);
    }

    @Test
    @DisplayName("createProduct - Throws ResourceNotFoundException when category does not exist")
    void createProduct_CategoryNotFound() {
        ProductCreateRequest request = ProductCreateRequest.builder()
                .name("Hyaluronic Acid Serum")
                .categoryId(99L)
                .build();

        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.createProduct(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category not found with ID: 99");

        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("getProductById - Successfully returns product response")
    void getProductById_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        when(productMapper.toResponse(sampleProduct)).thenReturn(sampleProductResponse);

        ProductResponse response = productService.getProductById(1L);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getBrand()).isEqualTo("The Ordinary");
    }

    @Test
    @DisplayName("getProductById - Throws ResourceNotFoundException when product not found")
    void getProductById_NotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProductById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Product not found with ID: 999");
    }

    @Test
    @DisplayName("updateProduct - Successfully updates existing product")
    void updateProduct_Success() {
        ProductUpdateRequest request = ProductUpdateRequest.builder()
                .name("Updated Serum")
                .brand("The Ordinary")
                .price(new BigDecimal("9.99"))
                .stock(40)
                .skinType(SkinType.ALL)
                .categoryId(1L)
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(sampleCategory));
        doAnswer(invocation -> {
            sampleProduct.setName("Updated Serum");
            return null;
        }).when(productMapper).updateEntityFromRequest(request, sampleProduct, sampleCategory);
        when(productRepository.save(sampleProduct)).thenReturn(sampleProduct);

        ProductResponse updatedResponse = ProductResponse.builder()
                .id(1L)
                .name("Updated Serum")
                .brand("The Ordinary")
                .category(sampleCategoryResponse)
                .build();
        when(productMapper.toResponse(sampleProduct)).thenReturn(updatedResponse);

        ProductResponse result = productService.updateProduct(1L, request);

        assertThat(result.getName()).isEqualTo("Updated Serum");
        verify(productRepository, times(1)).save(sampleProduct);
    }

    @Test
    @DisplayName("deleteProduct - Successfully deletes product and removes associated image")
    void deleteProduct_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));

        productService.deleteProduct(1L);

        verify(fileStorageService, times(1)).deleteFile("/uploads/sample.png");
        verify(productRepository, times(1)).delete(sampleProduct);
    }

    @Test
    @DisplayName("uploadProductImage - Successfully stores image and updates product imageUrl")
    void uploadProductImage_Success() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "serum.png", "image/png", "fake image bytes".getBytes()
        );

        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        when(fileStorageService.storeFile(file)).thenReturn("/uploads/new-uuid.png");
        when(productRepository.save(sampleProduct)).thenReturn(sampleProduct);

        ImageUploadResponse response = productService.uploadProductImage(1L, file);

        assertThat(response).isNotNull();
        assertThat(response.getProductId()).isEqualTo(1L);
        assertThat(response.getImageUrl()).isEqualTo("/uploads/new-uuid.png");
        assertThat(sampleProduct.getImageUrl()).isEqualTo("/uploads/new-uuid.png");
        verify(fileStorageService, times(1)).deleteFile("/uploads/sample.png");
    }
}
