package com.pdas.priCart.shop.auth.user.service;

import com.pdas.priCart.shop.auth.user.dto.CreateUserRequestDto;
import com.pdas.priCart.shop.auth.user.dto.UserDto;
import com.pdas.priCart.shop.auth.user.dto.UserUpdateDto;
import com.pdas.priCart.shop.auth.user.models.User;

public interface IUserService {
    User getUserById(Long userId);
    User createUser(CreateUserRequestDto request);
    User updateUser(UserUpdateDto request, Long userId);
    void deleteUser(Long userId);

    UserDto convertUserToDto(User user);
}
