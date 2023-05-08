package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.example.dto.token.RefreshTokenRequest;
import org.example.dto.token.TokenRequest;
import org.example.dto.token.TokenResponse;
import org.example.dto.user.CreateUserDTO;
import org.example.service.AuthUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthUserService authUserService;

    @Operation(summary = "This API is used for registration",
            description = "This API is used for registration")
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @NonNull @Valid CreateUserDTO dto
    ) {
        authUserService.register(dto);
        return ResponseEntity.ok("User registered successfully");
    }


    @Operation(summary = "This API is used for login in",
            description = "This API is used for login in")
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            @NonNull String username,
            @NonNull String password
    ) {
        return ResponseEntity.ok(authUserService.login(username, password));
    }


    @Operation(summary = "This API is used for getting access token",
            description = "This API is used for getting access token")
    @GetMapping("/access/token")
    public ResponseEntity<TokenResponse> generateToken(TokenRequest tokenRequest) {
        return ResponseEntity.ok(authUserService.generateToken(tokenRequest));
    }


    @Operation(summary = "This API is used for getting refresh token",
            description = "This API is used for getting refresh token")
    @GetMapping("/refresh/token")
    public ResponseEntity<TokenResponse> refreshToken(RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(authUserService.refreshToken(refreshTokenRequest));
    }
}
