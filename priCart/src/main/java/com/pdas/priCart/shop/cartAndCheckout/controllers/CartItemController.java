package com.pdas.priCart.shop.cartAndCheckout.controllers;

import com.pdas.priCart.shop.cartAndCheckout.dtos.CartAddResponse;
import com.pdas.priCart.shop.cartAndCheckout.dtos.CartItemDto;
import com.pdas.priCart.shop.cartAndCheckout.mappers.CartMapper;
import com.pdas.priCart.shop.cartAndCheckout.models.Cart;
import com.pdas.priCart.shop.cartAndCheckout.services.CartItemService;
import com.pdas.priCart.shop.cartAndCheckout.services.CartService;
import com.pdas.priCart.shop.common.dto.ApiResponse;
import com.pdas.priCart.shop.product.exception.ProductNotFoundException;
import com.pdas.priCart.shop.product.exception.ResourceNotFoundException;
import com.pdas.priCart.shop.user.models.User;
import com.pdas.priCart.shop.user.service.UserService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api_prefix}/cartItems")
public class CartItemController {

    private final CartItemService cartItemService;
    private final CartService cartService;
    private final CartMapper cartMapper;
    private final UserService userService;

    @PostMapping("/item/add")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestParam Long productId, @RequestParam Integer quantity){
        try {
            User user = userService.getAuthenticatedUser();
            Cart cart = cartService.initializeNewCart(user);
            cart = cartItemService.addCartItem(cart.getId(), productId, quantity);
            List<CartItemDto> itemDtos = cart.getCartItems().stream()
                    .filter(item -> !item.isDeleted())
                    .map(cartItem ->
                        CartItemDto.builder()
                                .productName(cartItem.getProduct().getName())
                                .productId(cartItem.getProduct().getId())
                                .quantity(cartItem.getQuantity())
                                .unitPrice(cartItem.getUnitPrice())
                                .subTotal(cartItem.getTotalPrice())
                                .build()

                    ).toList();
            CartAddResponse response = CartAddResponse.builder()
                    .cartId(cart.getId())
                    .totalItems(cart.getCartItems().size())
                    .totalAmount(cart.getTotalAmount())
                    .items(itemDtos)
                    .build();
            return ResponseEntity.ok().body(new ApiResponse<>("Item added to cart", response));

        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch(JwtException e) {
            return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{cartId}/item/{itemId}")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId, @PathVariable Long itemId){
        try {
            Cart updatedCart  = cartItemService.removeCartItem(cartId, itemId);
            return ResponseEntity.ok().body(new ApiResponse<>("Item removed from cart: ", cartMapper.toDto(updatedCart)));
        } catch (ResourceNotFoundException | ProductNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse<>(e.getMessage(), null));
        }
    }

    @PutMapping("/item/{itemId}")
    public ResponseEntity<ApiResponse> updateQuantity(@PathVariable Long itemId, @RequestParam Integer quantity){
        try {
            cartItemService.updateItemQuantity(itemId, quantity);
            return ResponseEntity.ok().body(new ApiResponse<>("Item updated", null));
        } catch (ProductNotFoundException | ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch(JwtException e) {
            return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
