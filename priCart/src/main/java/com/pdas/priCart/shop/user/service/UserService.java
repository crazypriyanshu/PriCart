package com.pdas.priCart.shop.user.service;

import com.pdas.priCart.shop.product.exception.ResourceNotFoundException;
import com.pdas.priCart.shop.user.dto.CreateUserRequestDto;
import com.pdas.priCart.shop.user.dto.ResetPasswordDto;
import com.pdas.priCart.shop.user.dto.UserDto;
import com.pdas.priCart.shop.user.dto.UserUpdateDto;
import com.pdas.priCart.shop.user.exceptions.UserNotFoundException;
import com.pdas.priCart.shop.user.models.User;
import com.pdas.priCart.shop.user.models.UserMapper;
import com.pdas.priCart.shop.user.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserService implements IUserService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public User createUser(CreateUserRequestDto request) {
        return Optional.of(request)
                .filter(user -> !userRepository.existsByEmail(request.getEmailId()))
                .map(userRequest -> {
                    User user = new User();
                    user.setEmail(userRequest.getEmailId());
                    user.setFirstName(userRequest.getFirstName());
                    user.setLastName(userRequest.getLastName());
                    user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
                    return userRepository.save(user);
                }).orElseThrow(() -> new RuntimeException("Unable to save the user"));
    }

    @Override
    public User updateUser(UserUpdateDto request, Long userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    user.setFirstName(request.getFirstName());
                    user.setLastName(request.getLastName());
                    return userRepository.save(user);
                }).orElseThrow(() -> new RuntimeException("Unable to update user"));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).ifPresentOrElse(userRepository::delete, () -> {
            throw new RuntimeException("User not found to delete");
        });

    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found in getAuthenticatedUser: "+email));
    }

    @Override
    public void resetPassword(ResetPasswordDto resetPasswordDto) {
        if (!resetPasswordDto.getNewPassword().equals(resetPasswordDto.getConfirmPassword())){
            throw new IllegalStateException("Passwords donot match, make sure you enter correctly");
        }

        try {
            User user = userRepository.findByEmail(resetPasswordDto.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User not found with email: "+resetPasswordDto.getEmail()));
            user.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
            userRepository.save(user);
        } catch (Exception e) {
            throw new IllegalStateException("technical error while updating password of user: "+resetPasswordDto.getConfirmPassword());
        }
    }

    @Override
    public UserDto convertUserToDto(User user) {
        return userMapper.userToUserDto(user);
    }
}
