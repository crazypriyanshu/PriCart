package com.pdas.priCart.shop.product.exception;

public class CategoryAlreadyPresent extends RuntimeException{
    public CategoryAlreadyPresent(String message){
        super(message);
    }
}
