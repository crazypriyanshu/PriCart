package com.pdas.priCart.shop.product.services;

import com.pdas.priCart.shop.product.dto.AddProductRequest;
import com.pdas.priCart.shop.product.dto.ProductDto;
import com.pdas.priCart.shop.product.dto.ProductUpdateRequest;
import com.pdas.priCart.shop.product.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    ProductDto createProduct(AddProductRequest request);
    ProductDto updateProduct(Long productId, ProductUpdateRequest request);


    ProductDto getProductById(Long productId);

    Page<ProductDto> getAllProducts(Pageable pageable);
    Page<ProductDto> getProductsByCategory(String categoryName, Pageable pageable);
    Page<ProductDto> getProductsByBrand(String brand, Pageable pageable);
    Page<ProductDto> getProductsByCategoryAndBrand(String categoryName, String brand, Pageable pageable);
    Page<ProductDto> searchProductsByName(String name, Pageable pageable);



    void updateInventory(Long productId, Integer inventory);


    void deleteProduct(Long productId);

    boolean productExists(String name, String brand);

    long countProductsByBrandAndName(String brand, String name);

}
