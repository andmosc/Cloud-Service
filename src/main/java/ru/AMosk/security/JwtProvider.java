package ru.AMosk.security;

import com.sun.istack.NotNull;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;
/*
генерация токена
валидация токена
получаение payload
 */
@Component
public class JwtProvider {

    @Value("${jwt.secret.access}")
    private String jwtSecret;
    public String generateToken(@NotNull UserDetails user) {
        final Date accessExpiration = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(accessExpiration) // дата до которой токен валиден
                .signWith(SignatureAlgorithm.HS256,jwtSecret) // токен ключ
                .claim("roles", user.getAuthorities()) //роль
                .compact();
    }
//todo validationTokens
    public boolean validateToken(String token) {
        return false;
    }
//todo getClaims
    public Claims getClaims(String token) {
        return null;
    }
}
