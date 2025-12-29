package com.pdas.priCart.shop.auth.dtos;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class LoginRequest {
    private String username;
    private String password;
}
