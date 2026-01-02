package com.pdas.priCart.shop.cartAndCheckout.services.PaymentAdaptors;

import com.pdas.priCart.shop.cartAndCheckout.dtos.PaymentInitializationResponse;
import com.pdas.priCart.shop.cartAndCheckout.dtos.PaymentVerificationRequest;
import com.pdas.priCart.shop.cartAndCheckout.models.CurrencyCode;
import com.pdas.priCart.shop.cartAndCheckout.models.PaymentType;
import com.pdas.priCart.shop.cartAndCheckout.services.PaymentProcessor;
import com.pdas.priCart.shop.order.models.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service("RAZOR_PAY")
public class RazorPayServiceAdapter implements PaymentProcessor {

    private static final Logger log = LoggerFactory.getLogger(RazorPayServiceAdapter.class);
    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    @Override
    public String setUpGateway() {
        return PaymentType.RAZOR_PAY.toString();
    }

    @Override
    public PaymentInitializationResponse initiatePayment(Order order) {
        try {
            RazorpayClient client = new RazorpayClient(keyId, keySecret);

            JSONObject orderRequest = new JSONObject();
            // convert bu=ig decimal to paise
            long amountInPaise = order.getTotalAmount().multiply(new BigDecimal(100)).longValue();
            orderRequest.put("amount", amountInPaise);
            orderRequest.put("currency", CurrencyCode.INR.toString());
            orderRequest.put("receipt", "txn"+order.getId());
            log.info("Order request = {}", orderRequest);

            com.razorpay.Order razorpayOrder = client.orders.create(orderRequest);
            return PaymentInitializationResponse.builder()
                    .gatewayName(setUpGateway())
                    .orderId(razorpayOrder.get("id"))
                    .amount(amountInPaise)
                    .apiKey(keyId)
                    .build();
        } catch (RazorpayException e) {
            throw new RuntimeException("Razorpay Error: " + e.getMessage());
        }
    }

    public boolean verifyPayment(PaymentVerificationRequest request){
        try{
            String secret = keySecret;
            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", request.getRazorpayOrderId());
            options.put("razorpay_payment_id", request.getRazorpayPaymentId());
            options.put("razorpay_signature", request.getRazorpaySignature());
            return Utils.verifyPaymentSignature(options, secret);
        } catch (RazorpayException e) {
            log.error("Payment verification failed", e);
            return false;
        }
    }
}
