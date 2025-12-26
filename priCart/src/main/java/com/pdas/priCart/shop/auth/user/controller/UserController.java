package com.pdas.priCart.shop.auth.user.controller;

import com.pdas.priCart.shop.auth.user.dto.CreateUserRequestDto;
import com.pdas.priCart.shop.auth.user.dto.UserDto;
import com.pdas.priCart.shop.auth.user.dto.UserUpdateDto;
import com.pdas.priCart.shop.auth.user.exceptions.UserNotFoundException;
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
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            UserDto dto = userService.convertUserToDto(user);
            logger.info("Fetched successfully user details for : {}", userId );
            return ResponseEntity.ok(new ApiResponse("User fetched successfully", dto));
        } catch (UserNotFoundException e){
            logger.error("User not found : {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UserUpdateDto userUpdateDto, @PathVariable Long userId) {
        try {
            User user = userService.updateUser(userUpdateDto, userId);
            UserDto userDto = userService.convertUserToDto(user);
            logger.info("Updated user details successfully for : {}", userId );
            return ResponseEntity.ok(new ApiResponse("Update success", userDto));

        } catch (Exception e) {
            logger.error("Technical error while updating user with userId {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId){
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse("User Delete successful", null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
