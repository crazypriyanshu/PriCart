package com.pdas.priCart.shop.order.models;

import com.pdas.priCart.shop.common.models.BaseModel;
import com.pdas.priCart.shop.product.models.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "order_items")
public class OrderItem extends BaseModel {
    private int quantity;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public OrderItem(Order order, Product product, int quantity, BigDecimal price){
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }
}
