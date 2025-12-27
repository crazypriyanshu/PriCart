package com.pdas.priCart.shop.auth.user.service;

import com.pdas.priCart.shop.user.dto.CreateUserRequestDto;
import com.pdas.priCart.shop.user.models.User;
import com.pdas.priCart.shop.user.models.UserMapper;
import com.pdas.priCart.shop.user.repositories.UserRepository;
import com.pdas.priCart.shop.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private CreateUserRequestDto userRequestDto;

    @BeforeEach
    void setup(){
        userRequestDto = new CreateUserRequestDto();
        userRequestDto.setEmailId("abc@google.com");
        userRequestDto.setFirstName("Priyanshu");
        userRequestDto.setLastName("Das");
        userRequestDto.setPassword("ABCF");
    }

    @Test
    void getUserById() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("1Test");
        user.setEmail("tiger@abc.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User result = userService.getUserById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("1Test", result.getFirstName());
    }

    @Test
    void getUserById_whenUserDoesNotExists_shouldThrowException(){
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.getUserById(1L));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void createUserWhenEmailDoesNotExists() {
        when(userRepository.existsByEmail("abc@google.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        User savedUser = userService.createUser(userRequestDto);

        assertNotNull(savedUser);
        assertEquals("abc@google.com", savedUser.getEmail());
    }
}