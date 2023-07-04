package ru.AMosk.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.AMosk.exception.AuthException;
import ru.AMosk.security.JwtProvider;
import ru.AMosk.security.dto.AuthRequest;
import ru.AMosk.security.dto.AuthResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private  final AuthenticationManager authenticationManager;
    public final JwtProvider jwtProvider;
    private final TokenInMemory tokenInMemory;

    public AuthResponse login(AuthRequest authRequest) {
        Authentication authentication = getAuthentication(authRequest);
        UserDetails user = (UserDetails) authentication.getPrincipal();
        final String authToken = jwtProvider.generateToken(user);
        //todo redis
        tokenInMemory.addTokenUser(authToken,user);
        log.info("получен токен для '{}'", user.getUsername());
        return new AuthResponse(authToken);
    }

    private Authentication getAuthentication(AuthRequest authRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getLogin(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            log.error("Имя  '{}'  и пароль '{}' не правильны", authRequest.getLogin(), authRequest.getPassword());
            int id = 0;
            //todo id
            throw new AuthException("Имя или пароль неправильны", id);
        }
        return authentication;
    }
}
