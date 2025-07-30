package com.asrcore.sis_cakes_menu.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException() {super("Product not found.");}

    public ProductNotFoundException(String message) {
        super(message + " Product not found.");
    }
}
