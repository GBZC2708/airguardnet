package com.airguardnet.user.infrastructure.web;

import com.airguardnet.common.web.ApiResponse;
import com.airguardnet.user.application.usecase.LoginResult;
import com.airguardnet.user.application.usecase.LoginUseCase;
import com.airguardnet.user.application.usecase.RegisterUserUseCase;
import com.airguardnet.user.domain.model.User;
import com.airguardnet.user.infrastructure.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final RegisterUserUseCase registerUserUseCase;
    private final JwtService jwtService;

    public AuthController(LoginUseCase loginUseCase, RegisterUserUseCase registerUserUseCase, JwtService jwtService) {
        this.loginUseCase = loginUseCase;
        this.registerUserUseCase = registerUserUseCase;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpServletRequest) {
        LoginResult result = loginUseCase.login(request.getEmail(), request.getPassword(), httpServletRequest.getRemoteAddr());
        String token = jwtService.generateToken(result.getUserId(), result.getRole(), result.getPlanId());
        LoginResponse response = new LoginResponse(token, result.getUserId(), result.getName(), result.getEmail(), result.getRole(), result.getPlanId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDTO>> register(@Valid @RequestBody CreateUserRequest request) {
        User user = registerUserUseCase.register(request.getName(), request.getLastName(), request.getEmail(), request.getPassword(), request.getRole(), request.getPlanId());
        return ResponseEntity.ok(ApiResponse.success(UserDTO.fromDomain(user)));
    }
}
