package org.example.quizMates.exception;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ExceptionResponse(
        String message,
        int statusCode,
        LocalDateTime timestamp
) {
}
