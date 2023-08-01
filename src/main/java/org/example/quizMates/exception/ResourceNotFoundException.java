package org.example.quizMates.exception;

public class ResourceNotFoundException extends RuntimeException {
    private final static String MSG = "Resource not found";

    public ResourceNotFoundException() {
        this(MSG);
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
