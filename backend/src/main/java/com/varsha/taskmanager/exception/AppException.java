package com.varsha.taskmanager.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

/**
 * General-purpose application exception that carries an HTTP status.
 * Used when an error doesn't fit the more specific exception types.
 */
@Getter
public class AppException extends RuntimeException {
    private final HttpStatus status;

    public AppException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
