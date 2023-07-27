package ru.AMosk.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.AMosk.enums.Role;
import ru.AMosk.exception.AuthException;
import ru.AMosk.repository.TokenRepository;
import ru.AMosk.security.JwtAuthentication;
import ru.AMosk.security.JwtToken;
import ru.AMosk.dto.security.AuthRequest;
import ru.AMosk.dto.security.AuthResponse;
import ru.AMosk.services.AuthService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    public final JwtToken jwtToken;
    public  final TokenRepository tokenRepository;
    private  final JwtAuthentication jwtAuthentication;

    @Override
    public AuthResponse login(AuthRequest authRequest) {
        Authentication authentication = getAuthentication(authRequest);

        UserDetails user =  (UserDetails) authentication.getPrincipal();

        List<String> collect = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        Set<Role> roleSet = collect.stream().map(Role::valueOf).collect(Collectors.toSet());
        jwtAuthentication.setRoles(roleSet);
        jwtAuthentication.setAuthenticated(true);
        jwtAuthentication.setName(user.getUsername());

        final String authToken = jwtToken.generateToken(jwtAuthentication);

        tokenRepository.save(authToken,jwtAuthentication);
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
