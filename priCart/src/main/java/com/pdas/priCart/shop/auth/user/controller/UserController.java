package com.pdas.priCart.shop.auth.user.controller;

import com.pdas.priCart.shop.auth.user.dto.CreateUserRequestDto;
import com.pdas.priCart.shop.auth.user.dto.UserDto;
import com.pdas.priCart.shop.auth.user.models.User;
import com.pdas.priCart.shop.auth.user.service.IUserService;
import com.pdas.priCart.shop.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/${api_prefix}/users")
@Tag(name = "User Management", description = "APIs for creating and managing users")
public class UserController {
    private final IUserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Operation(summary = "Create a new User")
    @PostMapping
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequestDto request){
        try {
            User user = userService.createUser(request);
            UserDto userDto = userService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("User created succesfully", userDto));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
