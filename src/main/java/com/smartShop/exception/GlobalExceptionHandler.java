package com.smartShop.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

// Centralise la gestion de TOUTES les exceptions du projet
@ControllerAdvice
public class GlobalExceptionHandler {

    // Méthode utilitaire pour construire la réponse JSON
    private ResponseEntity<ErrorResponse> buildErrorResponse(
            Exception ex, HttpStatus status, HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                ex.getClass().getSimpleName(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(error, status);
    }


    // 400 - Erreur de validation
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            ValidationException ex, HttpServletRequest request) {

        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request);
    }


    // 401 - Non authentifié
    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(
            UnauthorizedActionException ex, HttpServletRequest request) {

        return buildErrorResponse(ex, HttpStatus.UNAUTHORIZED, request);
    }







}
