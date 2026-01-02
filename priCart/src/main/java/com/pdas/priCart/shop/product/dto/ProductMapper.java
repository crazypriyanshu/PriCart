package com.pdas.priCart.shop.product.dto;

import com.pdas.priCart.shop.product.models.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "category.name", target = "categoryName")
    ProductDto productToDto(Product product);
    Product dtoToProduct(ProductDto productDto);
    List<ProductDto> productsToDtos(List<Product> products);
}
