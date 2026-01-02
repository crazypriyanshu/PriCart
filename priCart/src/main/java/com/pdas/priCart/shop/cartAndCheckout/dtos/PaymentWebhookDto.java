package com.pdas.priCart.shop.cartAndCheckout.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentWebhookDto {
    // IDs used for linking
    private String gatewayOrderId;    // To find the record in your DB
    private String gatewayPaymentId;  // The new 'pay_xxx' ID

    // Customer Info
    private String email;
    private String contact;

    // Payment Metadata
    private String method;            // e.g., "wallet", "card", "upi"
    private String wallet;            // e.g., "jiomoney", "paytm"
    private String bank;              // e.g., "HDFC"

    // Financial Details (Stored in Paise/Cents as Long/Integer)
    private Long amount;
    private Integer tax;
    private Integer fee;
}
