package ru.AMosk.services;

import ru.AMosk.dto.security.AuthRequest;
import ru.AMosk.dto.security.AuthResponse;

public interface AuthService {
    AuthResponse login(AuthRequest authRequest);
}
