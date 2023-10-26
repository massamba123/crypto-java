package com.farhan.staradmin.configuration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception ex) {
        String errorMessage = "Une erreur est survenue lors de l'opération. Veuillez consulter la documentation pour plus de détails.";
        return ResponseEntity.ok().body(errorMessage);
    }
}
