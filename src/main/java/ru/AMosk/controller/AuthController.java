package ru.AMosk.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.AMosk.security.dto.AuthRequest;
import ru.AMosk.security.dto.AuthResponse;
import ru.AMosk.services.AuthService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse createAuthenticationToken(@RequestBody AuthRequest authRequest) {
        log.info("login");
        return authService.login(authRequest);
    }
    @GetMapping ("/test")
    @ResponseStatus(HttpStatus.OK)
    public String test() {
        log.info("test");
        return "test";
    }
    @PostMapping("/logout")
    public String logout(@RequestHeader("auth-token") String token) {
        return "logout";
    }

}