package com.pdas.priCart.shop.order.service;

import com.pdas.priCart.shop.cartAndCheckout.dtos.PaymentWebhookDto;
import com.pdas.priCart.shop.cartAndCheckout.models.Cart;
import com.pdas.priCart.shop.cartAndCheckout.services.CartService;
import com.pdas.priCart.shop.order.dto.OrderDto;
import com.pdas.priCart.shop.order.dto.OrderItemDto;
import com.pdas.priCart.shop.order.exceptions.ProductOutOfStockException;
import com.pdas.priCart.shop.order.mapper.OrderMapper;
import com.pdas.priCart.shop.order.models.Order;
import com.pdas.priCart.shop.order.models.OrderItem;
import com.pdas.priCart.shop.order.models.OrderStatus;
import com.pdas.priCart.shop.order.models.PaymentDetails;
import com.pdas.priCart.shop.order.orderRepository.OrderRepository;
import com.pdas.priCart.shop.order.orderRepository.PaymentDetailsRepository;
import com.pdas.priCart.shop.product.exception.ProductNotFoundException;
import com.pdas.priCart.shop.product.exception.ResourceNotFoundException;
import com.pdas.priCart.shop.product.models.Product;
import com.pdas.priCart.shop.product.repositories.ProductRepository;
import com.pdas.priCart.shop.user.models.User;
import com.pdas.priCart.shop.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final UserService userService;
    private final PaymentDetailsRepository paymentDetailRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional // Ensures Atomicity
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId)
                .orElseGet(() -> {
                    User user = userService.getAuthenticatedUser();
                    return cartService.initializeNewCart(user);
                });



        // Initialize Order
        Order order = createOrderFromCart(cart);

        //Transform CartItems to OrderItems and link them
        List<OrderItem> orderItemList = createOrderItems(order, cart);

        // update Order State
        order.setOrderItems(new HashSet<>(orderItemList));
        order.setTotalAmount(calculateTotalAmount(orderItemList));

        // Save (order items are saved via CascadingType.ALL)
        Order saveOrder = orderRepository.save(order);

        return order;
    }

    // Sort of a static factory method
    private static Order createOrderFromCart(Cart cart){
        Order order = new Order();
        //1. check if teh cart has a user of not
        if (cart.getUser() == null){
            throw new IllegalStateException("Cannot create a cart without a user");
        }
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());
        // 2. Convert CartItems to OrderItems
        Set<OrderItem> orderItems = cart.getCartItems().stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());

            // If the product price changes later, the order history stays accurate.
            orderItem.setPrice(cartItem.getProduct().getPrice());

            // Link the item to this order (for JPA Bi-directional relationship)
            orderItem.setOrder(order);
            return orderItem;
        }).collect(Collectors.toSet());
        order.setOrderItems(orderItems);

        // 3. Set the total amount from the cart
        order.setTotalAmount(cart.getTotalAmount());

        return order;
    }



//    private BigDecimal calculateTotalAMount(List<OrderItem> orderItemList){
//        return orderItemList.stream()
//                .map(orderItem -> {orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()));})
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(this::getItemTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal getItemTotal(OrderItem item) {
        if (item.getPrice() == null) return BigDecimal.ZERO;
        return item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart){
        return cart.getCartItems()
                .stream()
                .map(cartItem -> {
                    Product product = cartItem.getProduct();
                    Integer stock = product.getInventory();
                    // 1. Address null or zero inventory
                    if (stock == null || stock <= 0) {
                        throw new ProductOutOfStockException(
                                "The item '" + product.getName() + "' is currently out of stock. It will be back soon!"
                        );
                    }

                    // 2. Check if they are asking for more than we have
                    if (stock < cartItem.getQuantity()) {
                        throw new IllegalStateException(
                                "Only " + stock + " units of '" + product.getName() + "' available. Please adjust your cart."
                        );
                    }

                    // 3. Safely deduct stock now
                    product.setInventory(product.getInventory() - cartItem.getQuantity());
                    productRepository.save(product);
                    OrderItem orderItem = new OrderItem();
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setProduct(cartItem.getProduct());
                    orderItem.setPrice(cartItem.getUnitPrice());
                    orderItem.setOrder(order);
                    return orderItem;
                })
                .toList();
    }

    @Override
    public OrderDto getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("No orders found with orderId: "+orderId));
    }

    @Override
    public List<OrderDto> getOrderByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(this::convertToDto).toList();
    }

    @Override
    public OrderDto convertToDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setUserId(order.getUser().getId());
        orderDto.setOrderDate(order.getOrderDate());
        orderDto.setTotalAmount(order.getTotalAmount());
        orderDto.setStatus(order.getOrderStatus().toString());

        List<OrderItemDto> itemDtos = order.getOrderItems().stream().map(item -> {
            OrderItemDto itemDto = new OrderItemDto();
            itemDto.setQuantity(item.getQuantity());
            itemDto.setPrice(item.getPrice());

            if (item.getProduct() != null) {
                itemDto.setProductId(item.getProduct().getId());
                itemDto.setProductName(item.getProduct().getName());
                itemDto.setProductBrand(item.getProduct().getBrand());
            }
            return itemDto;
        }).toList();

        orderDto.setOrderItems(itemDtos);
        return orderDto;
    }

    @Transactional
    public void completeOrder(PaymentWebhookDto dto){
        // 1. Find the existing record by the Order ID (saved during checkout initialization)
        PaymentDetails payment = paymentDetailRepository.findByGatewayOrderId(dto.getGatewayOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("No payment record found for: " + dto.getGatewayOrderId()));

        // 2. Map all the 'Good Info' to the entity
        payment.setGatewayPaymentId(dto.getGatewayPaymentId());
        payment.setEmail(dto.getEmail());
        payment.setContact(dto.getContact());
        payment.setMethod(dto.getMethod());
        payment.setWallet(dto.getWallet());
        payment.setBank(dto.getBank());
        payment.setAmount(dto.getAmount());
        payment.setTax(dto.getTax());
        payment.setFee(dto.getFee());
        payment.setStatus("CAPTURED");

        // 3. Update Order Status
        Order order = payment.getOrder();
        order.setOrderStatus(OrderStatus.PAID);

        // 4. Save and Flush
        paymentDetailRepository.save(payment);
        orderRepository.save(order);

        // 5. Cleanup
        cartService.clearCart(order.getUser().getId());
    }

    @Transactional
    public void reduceInventory(Set<OrderItem> items){
        for (OrderItem orderItem: items){
            Product product = orderItem.getProduct();
            int quantitySold = orderItem.getQuantity();
            if (product.getInventory() < quantitySold) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }
            product.setInventory(product.getInventory() - quantitySold);
            productRepository.save(product);
        }
    }
}
