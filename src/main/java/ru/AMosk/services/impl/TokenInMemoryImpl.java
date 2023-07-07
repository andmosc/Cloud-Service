package ru.AMosk.services.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.AMosk.services.TokenInMemory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Data
@RequiredArgsConstructor
public class TokenInMemoryImpl implements TokenInMemory {

    private final Map<String, UserDetails> tokenInMemory = new ConcurrentHashMap<>();

    @Override
    public UserDetails getUserByToken(String token) {
        return tokenInMemory.get(token);
    }

    @Override
    public void addTokenUser(String token, UserDetails user) {
        tokenInMemory.put(token, user);
    }

    @Override
    public UserDetails dellTokenUser(String token) {
        return tokenInMemory.remove(token);
    }
}
