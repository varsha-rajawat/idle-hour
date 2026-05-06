package com.varsha.taskmanager.exception;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Validation Errors (@Valid on DTOs)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Validation Failed");

        // Collect all field errors into a map
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        problem.setProperty("errors", errors);
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    // ── Resource not found ─────────────────────────────────────
    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleNotFound(ResourceNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Resource Not Found");
        problem.setType(URI.create("about:not-found"));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    // ── Duplicate resource (e.g. email already in use) ─────────
    @ExceptionHandler(DuplicateResourceException.class)
    public ProblemDetail handleDuplicate(DuplicateResourceException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, ex.getMessage());
        problem.setTitle("Conflict");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    // ── Bad credentials (wrong password) ──────────────────────
    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentials(BadCredentialsException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED, "Invalid email or password");
        problem.setTitle("Authentication Failed");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    // ── Forbidden (correct token, wrong role) ─────────────────
    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDenied(AccessDeniedException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.FORBIDDEN, "You don't have permission to perform this action");
        problem.setTitle("Forbidden");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    // ── Generic app errors ─────────────────────────────────────
    @ExceptionHandler(AppException.class)
    public ProblemDetail handleAppException(AppException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                ex.getStatus(), ex.getMessage());
        problem.setTitle("Application Error");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    // ── Catch-all ──────────────────────────────────────────────
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex) {
        // Log the full exception internally but return a vague message to the client.
        // Never expose internal stack traces in production responses.
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred. Please try again later.");
        problem.setTitle("Internal Server Error");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }
}
