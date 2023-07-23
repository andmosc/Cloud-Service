package ru.AMosk.security;

import com.sun.istack.NotNull;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.AMosk.repository.TokenRepository;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/*
генерация токена
валидация токена
получаение payload
 */
@Component
@Slf4j
public class JwtToken {
    private final static int TOKEN_TIME_LIFE = 60;
    private final SecretKey jwtSecret;
    private final TokenRepository tokenRepository;

    public JwtToken(@Value("${jwt.secret.access}") String jwtSecret, TokenRepository tokenRepository) {
        this.jwtSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
        this.tokenRepository = tokenRepository;
    }

    public String generateToken(@NotNull JwtAuthentication user) {
        final Date now = new Date();
        final Date accessExpiration = Date.from(LocalDateTime.now().plusMinutes(TOKEN_TIME_LIFE)
                .atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setSubject(user.getName())
                .setIssuedAt(now)
                .setNotBefore(now)
                .setExpiration(accessExpiration) // дата до которой токен валиден
                .signWith(jwtSecret)
                .claim("roles", user.getAuthorities())
                .compact();
    }

    public JwtAuthentication validateToken(String token) {
        JwtAuthentication jwtAuthentication = tokenRepository.findUserByToken(token);
        if (jwtAuthentication != null) {
            try {
                Jwts.parserBuilder()
                        .setSigningKey(jwtSecret)
                        .build()
                        .parseClaimsJws(token);
                return jwtAuthentication;
            } catch (ExpiredJwtException expEx) {
                log.error("Срок действия токена истек (Token expired)", expEx);
            } catch (UnsupportedJwtException unsEx) {
                log.error("Форма токена неподдерживается jwt (Unsupported jwt)", unsEx);
            } catch (MalformedJwtException mjEx) {
                log.error("Форма токена некорректна для jwt (Malformed jwt)", mjEx);
            } catch (SignatureException sEx) {
                log.error("Недействительная подпись (Invalid signature)", sEx);
            } catch (Exception e) {
                log.error("Недопустимый токен (Invalid token)", e);
            }
        }
        log.warn("There is no user with such a token '{}'", token);
        return null;
    }

    private Claims getClaims(@NonNull String token, @NonNull Key secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
