package com.example.app.controllers;

import com.example.app.dtos.LoginRequest;
import com.example.app.dtos.LoginResponse;
import com.example.app.dtos.RegisterRequest;
import com.example.app.dtos.UserLoggedDto;
import com.example.app.models.User;
import com.example.app.repositories.UserRepository;
import com.example.app.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor

public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest);
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @CookieValue(name = "access_token", required = false)
            String access,
            @CookieValue(name = "refresh_token", required = false)
            String refresh,
            @RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest, access,refresh);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(
            @CookieValue(name = "refresh_token", required = false)
            String refresh) {
        return authService.refresh(refresh);
    }


    @PostMapping("/logout")
    public ResponseEntity<LoginResponse> logout(
            @CookieValue(name = "access_token", defaultValue = "") String accessToken) {
        return authService.logout(accessToken);
    }


    @GetMapping("/me")
    public ResponseEntity<UserLoggedDto> getCurrentUser() {
        UserLoggedDto userInfo = authService.getInfo();
        return ResponseEntity.ok(userInfo);
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkAuth() {
        return ResponseEntity.ok(Map.of("authenticated", true));
    }


}
