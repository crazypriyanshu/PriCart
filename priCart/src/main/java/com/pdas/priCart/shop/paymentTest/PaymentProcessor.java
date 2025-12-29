package com.pdas.priCart.shop.paymentTest;

public interface PaymentProcessor {
    public void makePayment(Long amount);
    public String getGateway();
}
