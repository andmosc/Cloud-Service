package ru.AMosk.security;

import com.sun.istack.NotNull;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.AMosk.services.TokenInMemory;

import java.time.ZonedDateTime;
import java.util.Date;

/*
генерация токена
валидация токена
получаение payload
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {

    @Value("${jwt.secret.access}")
    private String jwtSecret;
    private final TokenInMemory tokenInMemory;

    public String generateToken(@NotNull UserDetails user) {
        final Date accessExpiration = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(accessExpiration) // дата до которой токен валиден
                .signWith(SignatureAlgorithm.HS256, jwtSecret) // токен ключ
                .claim("roles", user.getAuthorities())
                .compact();
    }

    public UserDetails validateToken(String token) {
        UserDetails user = tokenInMemory.getUserByToken(token);
        if (user != null) {
            try {
                Jwts.parser()
                        .setSigningKey(jwtSecret)
                        .parseClaimsJws(token);
                return user;
            } catch (ExpiredJwtException expEx) { // если время подписи истекло
                log.error("Token expired", expEx);
            } catch (UnsupportedJwtException unsEx) {
                log.error("Unsupported jwt", unsEx);
            } catch (MalformedJwtException mjEx) { // если подпись некорректная (не парсится)
                log.error("Malformed jwt", mjEx);
            } catch (SignatureException sEx) { // если подпись не совпадает с вычисленной
                log.error("Invalid signature", sEx);
            } catch (Exception e) {
                log.error("invalid token", e);
            }
        }
        log.warn("There is no user with such a token '{}'", token);
        return null;
    }

    //todo getClaims
    public Claims getClaims(String token) {
        return null;
    }
}
