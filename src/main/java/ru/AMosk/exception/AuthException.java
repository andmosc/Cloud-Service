package ru.AMosk.exception;

import lombok.Getter;

@Getter
public class AuthException extends RuntimeException {

    private final int id;

    public AuthException(String message, int id) {
        super(message);
        this.id = id;
    }
}
