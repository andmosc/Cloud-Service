package ru.AMosk.services;

import org.springframework.security.core.userdetails.UserDetails;

public interface TokenInMemory {
    UserDetails getUserByToken(String token);
    void addTokenUser(String token, UserDetails user);
    UserDetails dellTokenUser(String token);
}
