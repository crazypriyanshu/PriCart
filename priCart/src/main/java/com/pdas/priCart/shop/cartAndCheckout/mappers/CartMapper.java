package com.pdas.priCart.shop.cartAndCheckout.mappers;

import com.pdas.priCart.shop.cartAndCheckout.dtos.CartAddResponse;
import com.pdas.priCart.shop.cartAndCheckout.dtos.CartItemDto;
import com.pdas.priCart.shop.cartAndCheckout.models.Cart;
import com.pdas.priCart.shop.cartAndCheckout.models.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(source = "id", target = "cartId")
    @Mapping(target = "items", expression = "java(mapActiveItems(cart.getCartItems()))")
    @Mapping(target = "totalItems", expression = "java(countActiveItems(cart.getCartItems()))")
    CartAddResponse toDto(Cart cart);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "totalPrice", target = "subTotal")
    CartItemDto toItemDto(CartItem item);

    default List<CartItemDto> mapActiveItems(Set<CartItem> items) {
        if (items == null) return List.of();
        return items.stream()
                .filter(item -> !item.isDeleted())
                .map(this::toItemDto)
                .collect(Collectors.toList());
    }

    // Custom logic to count only non-deleted items
    default int countActiveItems(Set<CartItem> items) {
        if (items == null) return 0;
        return (int) items.stream()
                .filter(item -> !item.isDeleted())
                .count();
    }
}
