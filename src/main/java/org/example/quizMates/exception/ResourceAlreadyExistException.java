package org.example.quizMates.exception;

public class ResourceAlreadyExistException extends RuntimeException {
    private final static String MSG = "Resource already exist";

    public ResourceAlreadyExistException() {
        this(MSG);
    }

    public ResourceAlreadyExistException(String message) {
        super(message);
    }
}
