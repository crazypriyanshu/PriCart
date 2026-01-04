package com.pdas.priCart.shop.cartAndCheckout.exceptions;

public class StockInsufficientException extends RuntimeException{
    public StockInsufficientException(String message){
        super(message);
    }
}
