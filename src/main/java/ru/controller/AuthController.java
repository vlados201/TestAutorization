package ru.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.service.UserService;
import ru.dto.RegisterUserRequestDto;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Зарегистрироваться")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterUserRequestDto request) {
        userService.registerUser(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    @Operation(summary = "Войти в аккаунт")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request) {

        boolean isAuthorized = userService.checkCredentials(
                request.getUsername(),
                request.getPassword()
        );

        if (!isAuthorized) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Неверный логин или пароль");
        }

        return ResponseEntity.ok("Авторизация успешна");
    }

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }
}
