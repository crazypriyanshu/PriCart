package com.pdas.priCart.shop.user.dto;

import com.pdas.priCart.shop.user.models.Role;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
}
