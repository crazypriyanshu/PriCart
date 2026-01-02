package com.pdas.priCart.shop.order.models;

import com.pdas.priCart.shop.common.models.BaseModel;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PaymentDetails extends BaseModel {
    private String gatewayOrderId;    // order_Rz8YBUXdEPfw3c
    private String gatewayPaymentId;  // pay_Rz8ZLLa3vGiDVa
    private String gatewayName;       // RAZORPAY
    private String status;            // captured

    private String email;             // abc@example.com
    private String contact;           // +918885557660
    private String method;            // wallet
    private String wallet;            // jiomoney
    private String bank;              // (null in this case)
    private Long amount;              // 847907 (in paise)
    private Integer tax;              // 3052
    private Integer fee;              // 20011        // PENDING, CAPTURED, FAILED

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
