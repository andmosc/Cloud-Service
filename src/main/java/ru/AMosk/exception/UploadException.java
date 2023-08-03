package ru.AMosk.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class UploadException extends RuntimeException {

    private final int id;

    public UploadException(String message, int id) {
        super(message);
        this.id = id;
        log.error("{}, id: {}",message,id);
    }
}
