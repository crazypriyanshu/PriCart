package com.pdas.priCart.shop.product.controller;

import com.pdas.priCart.shop.common.dto.ApiResponse;
import com.pdas.priCart.shop.product.dto.AddProductRequest;
import com.pdas.priCart.shop.product.dto.ProductDto;
import com.pdas.priCart.shop.product.dto.ProductUpdateRequest;
import com.pdas.priCart.shop.product.models.Product;
import com.pdas.priCart.shop.product.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api_prefix}/products")
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody @Valid AddProductRequest request) {
        ProductDto product = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long productId,
            @RequestBody @Valid ProductUpdateRequest request) {

        ProductDto product = productService.updateProduct(productId, request);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(
            @PathVariable Long productId) {

        ProductDto product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllProducts(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {

        Page<ProductDto> products = productService.getAllProducts(pageable);

        return ResponseEntity.ok(new ApiResponse("success", products));
    }

    @GetMapping("/category/{categoryName}")
    public ResponseEntity<Page<ProductDto>> getProductsByCategory(@PathVariable String categoryName, Pageable pageable) {
        return ResponseEntity.ok(productService.getProductsByCategory(categoryName, pageable));
    }

    @GetMapping("/brand/{brand}")
    public ResponseEntity<Page<ProductDto>> getProductsByBrand(@PathVariable String brand, Pageable pageable) {
        return ResponseEntity.ok(productService.getProductsByBrand(brand, pageable));
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<ProductDto>> getProductsByCategoryAndBrand(@RequestParam String categoryName, @RequestParam String brand, Pageable pageable) {
        return ResponseEntity.ok(productService.getProductsByCategoryAndBrand(categoryName, brand, pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductDto>> searchProductsByName(@RequestParam String name, Pageable pageable) {
        return ResponseEntity.ok(productService.searchProductsByName(name, pageable));
    }

    @PatchMapping("/{productId}/inventory")
    public ResponseEntity<Void> updateInventory(
            @PathVariable Long productId,
            @RequestParam Integer inventory) {

        productService.updateInventory(productId, inventory);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long productId) {

        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> productExists(
            @RequestParam String name,
            @RequestParam String brand) {

        return ResponseEntity.ok(
                productService.productExists(name, brand)
        );
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countProductsByBrandAndName(
            @RequestParam String brand,
            @RequestParam String name) {

        return ResponseEntity.ok(
                productService.countProductsByBrandAndName(brand, name)
        );
    }




}
