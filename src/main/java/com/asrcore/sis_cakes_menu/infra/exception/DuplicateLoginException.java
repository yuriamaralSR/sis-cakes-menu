package com.asrcore.sis_cakes_menu.infra.exception;

public class DuplicateLoginException extends RuntimeException {
    public DuplicateLoginException() {
        super("Login already exists.");
    }

    public DuplicateLoginException(String message) {
        super(message);
    }
}
