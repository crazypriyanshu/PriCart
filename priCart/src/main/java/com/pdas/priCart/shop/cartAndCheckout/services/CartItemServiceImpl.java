package com.pdas.priCart.shop.cartAndCheckout.services;

import com.pdas.priCart.shop.cartAndCheckout.models.Cart;
import com.pdas.priCart.shop.cartAndCheckout.models.CartItem;
import com.pdas.priCart.shop.cartAndCheckout.repositories.CartItemRepository;
import com.pdas.priCart.shop.cartAndCheckout.repositories.CartRepository;
import com.pdas.priCart.shop.product.exception.ProductNotFoundException;
import com.pdas.priCart.shop.product.exception.ResourceNotFoundException;
import com.pdas.priCart.shop.product.models.Product;
import com.pdas.priCart.shop.product.repositories.ProductRepository;
import com.pdas.priCart.shop.product.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class CartItemServiceImpl implements CartItemService{

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final CartService cartService;

    @Override
    @Transactional // Makes sure that entire process is atomic
    public Cart addCartItem(Long cartId, Long productId, int quantity) throws ProductNotFoundException {

        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: "+cartId));

        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException(("Product not found with ID: "+productId)));

        // Find existing item
        CartItem cartItem = cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseGet(() -> createNewCartItem(cart, product));

        int newQuantity = (cartItem.getId() == null) ? quantity : cartItem.getQuantity()+ quantity;
        if (product.getInventory() < newQuantity || product.getInventory() == null){
            throw new IllegalStateException("Requested quantity exceeds available stock for : "+ product.getName());
        }
        cartItem.setQuantity(newQuantity);
        cartItem.setUnitPrice(product.getPrice());
        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        // we are not saving this cartItem, because Cart entity has CascadeType.ALL, so whenever we update Cart, automatically do it on CartItems
        cartRepository.save(cart);
        return cart;

    }

    private CartItem createNewCartItem(Cart cart, Product product){
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(0);
        cartItem.setUnitPrice(product.getPrice());
        cartItem.setTotalPrice(BigDecimal.ZERO);
        return cartItem;
    }

    @Override
    @Transactional
    public Cart removeCartItem(Long cartId, Long productId) {

        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart not found with cartID: "+cartId));
        CartItem itemsToRemove = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId) && !item.isDeleted())
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item not found in the cart"));

        // remove the cart's internal collection
        itemsToRemove.setDeleted(true);
        // update teh cart total
        cart.updateTotalAmount();
        return cartRepository.save(cart);
    }


    @Override
    public Cart updateItemQuantity(Long productId, int quantity) {
        Cart cart = cartService.getCart();
        Product product = productRepository.getProductById(productId);
        cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId) && !item.isDeleted())
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(quantity);
                    item.setUnitPrice(product.getPrice());
                    item.setTotalPrice();
                });
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);

    }


    @Override
    public CartItem getCartItem(Long productId) {
        Cart cart = cartService.getCart();
        return cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId)&& !item.isDeleted())
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: "+ productId));
    }
}
