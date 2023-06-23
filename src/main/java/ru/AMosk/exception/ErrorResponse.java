package ru.AMosk.exception;

import lombok.Getter;

@Getter
public final class ErrorResponse {

    private static ErrorResponse instance;
    private String message;
    private Integer id;

    public static ErrorResponse getInstance() {
        if (instance == null) {
            instance = new ErrorResponse();
        }
        return instance;
    }

    public void setMessage(String message, Integer id) {
        this.id = id;
        this.message = message;
    }
}
