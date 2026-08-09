package com.yuvitra.inventory.authentication;

import com.yuvitra.inventory.common.response.ApiResponse;
import com.yuvitra.inventory.dto.request.LoginRequest;
import com.yuvitra.inventory.dto.request.RegisterRequest;
import com.yuvitra.inventory.dto.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.yuvitra.inventory.security.jwt.JwtService;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtService jwtService;
    private final AuthService authService;
    @GetMapping("/token-test")
    public String tokenTest(
            @RequestParam String token) {

        return jwtService.extractEmail(token);
    }
    @GetMapping("/profile")
    public String profile() {
        return "Authenticated User";
    }
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @RequestBody RegisterRequest request) {

        AuthResponse response = authService.register(request);

        ApiResponse<AuthResponse> apiResponse =
                ApiResponse.<AuthResponse>builder()
                        .success(true)
                        .message("Registration Successful")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(apiResponse);
    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @RequestBody LoginRequest request) {

        AuthResponse response = authService.login(request);

        ApiResponse<AuthResponse> apiResponse =
                ApiResponse.<AuthResponse>builder()
                        .success(true)
                        .message("Login Successful")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(apiResponse);
    }
}