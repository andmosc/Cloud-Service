package ru.AMosk.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.AMosk.repository.TokenRepository;
import ru.AMosk.security.JwtAuthentication;

@Repository
@RequiredArgsConstructor
public class TokenRepositoryImpl implements TokenRepository {

    private final RedisTemplate<String,JwtAuthentication> redisTemplate;
    @Override
    public void save(String token, JwtAuthentication JwtAuthentication) {
        redisTemplate.opsForValue().set(token,JwtAuthentication);
    }

    @Override
    public boolean delete(String token) {
        return Boolean.TRUE.equals(redisTemplate.delete(token));
    }

    @Override
    public JwtAuthentication findUserByToken(String token) {
        return redisTemplate.opsForValue().get(token);
    }
}
