package com.yuvitra.inventory.authentication;

import com.yuvitra.inventory.dto.request.LoginRequest;
import com.yuvitra.inventory.dto.request.RegisterRequest;
import com.yuvitra.inventory.dto.response.AuthResponse;
import com.yuvitra.inventory.entity.Role;
import com.yuvitra.inventory.entity.User;
import com.yuvitra.inventory.exception.DuplicateResourceException;
import com.yuvitra.inventory.exception.ResourceNotFoundException;
import com.yuvitra.inventory.exception.UnauthorizedException;
import com.yuvitra.inventory.repository.RoleRepository;
import com.yuvitra.inventory.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.yuvitra.inventory.security.jwt.JwtService;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    @Override
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(
                    "User already exists with email: " + request.getEmail()
            );
        }

        Role defaultRole = roleRepository.findByRoleName("STAFF")
                .orElseThrow(() ->
                        new ResourceNotFoundException("Default role STAFF not found"));

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        user.setPhone(request.getPhone());

        user.setActive(true);

        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        user.setRole(defaultRole);

        User savedUser = userRepository.save(user);

        return AuthResponse.builder()
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .role(savedUser.getRole().getRoleName())
                .message("User Registered Successfully")
                .token(null)
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new UnauthorizedException("Invalid Email or Password"));
        String token = jwtService.generateToken(
                user.getEmail()
        );
        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new UnauthorizedException(
                    "Invalid Email or Password");
        }

        return AuthResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole().getRoleName())
                .message("Login Successful")
                .token(token)
                .build();
    }
}