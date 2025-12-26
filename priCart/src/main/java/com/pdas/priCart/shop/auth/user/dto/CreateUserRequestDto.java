package com.pdas.priCart.shop.auth.user.dto;


import lombok.Data;

@Data
public class CreateUserRequestDto {
    private String firstName;
    private String lastName;
    private String emailId;
    private String password;
}
