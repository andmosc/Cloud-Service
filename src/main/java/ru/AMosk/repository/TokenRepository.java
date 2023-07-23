package ru.AMosk.repository;

import ru.AMosk.security.JwtAuthentication;

public interface TokenRepository {
    void save(String token, JwtAuthentication JwtAuthentication);

    boolean delete(String token);

    JwtAuthentication findUserByToken(String token);
}
