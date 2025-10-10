package com.asrcore.sis_cakes_menu.infra.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {super("User not found.");}

    public UserNotFoundException(String message) {
        super(message + " User not found.");
    }
}
