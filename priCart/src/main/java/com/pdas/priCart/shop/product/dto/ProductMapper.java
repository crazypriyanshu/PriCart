package com.pdas.priCart.shop.product.dto;

import com.pdas.priCart.shop.product.models.Product;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto productToDto(Product product);
    Product dtoToProduct(ProductDto productDto);
    List<ProductDto> productsToDtos(List<Product> products);
}
