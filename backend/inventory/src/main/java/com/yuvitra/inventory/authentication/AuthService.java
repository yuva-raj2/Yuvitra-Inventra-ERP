package com.yuvitra.inventory.authentication;

import com.yuvitra.inventory.dto.request.LoginRequest;
import com.yuvitra.inventory.dto.request.RegisterRequest;
import com.yuvitra.inventory.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}