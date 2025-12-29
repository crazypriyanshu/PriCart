package com.pdas.priCart.shop.auth.controller;

import com.pdas.priCart.shop.auth.dtos.LoginRequest;
import com.pdas.priCart.shop.auth.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;


    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(
                       loginRequest.getUsername(), loginRequest.getPassword()
               )
        );
        String token = tokenService.generateToken(authentication); // at this point we should get the real authentication token
        return ResponseEntity.ok(Map.of("token", token));
    }
}
