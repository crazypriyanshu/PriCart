package com.pdas.priCart.shop.auth.user.service;

import com.pdas.priCart.shop.auth.user.dto.CreateUserRequestDto;
import com.pdas.priCart.shop.auth.user.dto.UserDto;
import com.pdas.priCart.shop.auth.user.dto.UserUpdateDto;
import com.pdas.priCart.shop.auth.user.exceptions.UserNotFoundException;
import com.pdas.priCart.shop.auth.user.models.User;
import com.pdas.priCart.shop.auth.user.models.UserMapper;
import com.pdas.priCart.shop.auth.user.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserService implements IUserService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    UserService(UserRepository userRepository, UserMapper userMapper){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
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
                    user.setPassword(userRequest.getPassword());
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
    public UserDto convertUserToDto(User user) {
        return userMapper.userToUserDto(user);
    }
}
