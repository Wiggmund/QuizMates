package org.example.quizMates.exception;

public class DBInternalException extends RuntimeException {
    private final static String MSG = "Database internal exception has occured";

    public DBInternalException() {
        this(MSG);
    }

    public DBInternalException(String message) {
        super(message);
    }
}
