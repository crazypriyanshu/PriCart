package com.pdas.priCart.shop.paymentTest.adapters;

import com.pdas.priCart.shop.paymentTest.PaymentProcessor;
import org.springframework.stereotype.Component;

@Component
public class GpayServiceAdapter implements PaymentProcessor {
    public void makePayment(Long amount){
        System.out.println("Payment made by Gpay: "+amount);
    }

    public String getGateway(){
        return "Gpay";
    }
}
