package com.pdas.priCart.shop.auth.user.models;

import com.pdas.priCart.shop.auth.user.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto userToUserDto(User user);
    User userDtoToUser(UserDto userDto);
}
