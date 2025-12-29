package com.pdas.priCart.shop.paymentTest;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PaymentService {
    private Map<String, PaymentProcessor> paymentProcessorMap;


    public PaymentService(List<PaymentProcessor> paymentProcessors){
        paymentProcessorMap = paymentProcessors.stream()
                .collect(Collectors.toMap(PaymentProcessor::getGateway, Function.identity()));
    }

    public void processPayment(Long amount, String gateway){
        PaymentProcessor paymentProcessor = paymentProcessorMap.get(gateway);
        if (paymentProcessor == null){
            throw new IllegalArgumentException("Unsupported gateway");
        }
        paymentProcessor.makePayment(amount);
    }

}
