package ru.AMosk.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import ru.AMosk.repository.TokenRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {
    private static final String AUTHTOKEN = "auth-token";

    private final TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String token = request.getHeader(AUTHTOKEN).substring(7);
        if (tokenRepository.delete(token)) {
            log.info("Token {} was deleted from storage", token);
        } else {
            log.warn("Token {} was not deleted. There is no such record in token storage", token);
        }
    }
}
