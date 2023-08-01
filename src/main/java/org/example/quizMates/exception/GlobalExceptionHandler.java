package org.example.quizMates.exception;

import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;

public class GlobalExceptionHandler {
    public static ExceptionResponse handleException(RuntimeException initialException) {
        if (initialException instanceof ResourceNotFoundException exception) {
            return handleException(exception);
        }

        if (initialException instanceof ResourceAlreadyExistException exception) {
            return handleException(exception);
        }

        if (initialException instanceof DBInternalException exception) {
            return handleException(exception);
        }

        if (initialException instanceof IllegalArgumentException exception) {
            return handleException(exception);
        }

        return buildResponse(initialException.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    private static ExceptionResponse buildResponse(String message, int statusCode) {
        return ExceptionResponse.builder()
                .message(message)
                .statusCode(statusCode)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private static ExceptionResponse handleException(ResourceNotFoundException exception) {
        return buildResponse(exception.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
    }

    private static ExceptionResponse handleException(ResourceAlreadyExistException exception) {
        return buildResponse(exception.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
    }

    private static ExceptionResponse handleException(IllegalArgumentException exception) {
        return buildResponse(exception.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
    }

    private static ExceptionResponse handleException(DBInternalException exception) {
        return buildResponse(exception.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}
