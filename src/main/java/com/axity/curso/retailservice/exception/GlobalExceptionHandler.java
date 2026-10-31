package com.axity.curso.retailservice.exception;

import com.axity.curso.retailservice.dto.ErrorResponseDTO;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFound(
            RecursoNoEncontradoException ex,
            HttpServletRequest request) {

        return buildResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(StockInsuficienteException.class)
    public ResponseEntity<ErrorResponseDTO> handleStockInsuficiente(
            StockInsuficienteException ex,
            HttpServletRequest request) {

        return buildResponse(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(PrecioInvalidoException.class)
    public ResponseEntity<ErrorResponseDTO> handlePrecioInvalido(
            PrecioInvalidoException ex,
            HttpServletRequest request) {

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request
        );
    }

    private ResponseEntity<ErrorResponseDTO> buildResponse(
            HttpStatus status,
            String message,
            HttpServletRequest request) {

        ErrorResponseDTO response = new ErrorResponseDTO(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(response);
    }
}