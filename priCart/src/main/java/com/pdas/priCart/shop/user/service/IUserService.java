package com.pdas.priCart.shop.user.service;

import com.pdas.priCart.shop.user.dto.CreateUserRequestDto;
import com.pdas.priCart.shop.user.dto.ResetPasswordDto;
import com.pdas.priCart.shop.user.dto.UserDto;
import com.pdas.priCart.shop.user.dto.UserUpdateDto;
import com.pdas.priCart.shop.user.models.User;

public interface IUserService {
    User getUserById(Long userId);
    User createUser(CreateUserRequestDto request);
    User updateUser(UserUpdateDto request, Long userId);
    void deleteUser(Long userId);
    User getAuthenticatedUser();
    void resetPassword(ResetPasswordDto resetPasswordDto);

    UserDto convertUserToDto(User user);
}
