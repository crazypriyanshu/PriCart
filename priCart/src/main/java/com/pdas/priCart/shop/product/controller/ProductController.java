package com.pdas.priCart.shop.product.controller;

import com.pdas.priCart.shop.common.dto.ApiResponse;
import com.pdas.priCart.shop.product.dto.ProductDto;
import com.pdas.priCart.shop.product.models.Product;
import com.pdas.priCart.shop.product.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api_prefix}/products")
public class ProductController {
    private final ProductService productService;


    @GetMapping
    public ResponseEntity<ApiResponse> getAllProducts(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {

        Page<ProductDto> products = productService.getAllProducts(pageable);

        return ResponseEntity.ok(
                new ApiResponse("success", products)
        );
    }

}
