package com.agrihusac.SysInvent.utils;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Object> handleApiException(ApiException exception) {
        LOGGER.warn(exception.getErrorGenerico().getCodigo(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getErrorGenerico());
    }

    @ExceptionHandler(CustomNotFoundException.class)
    public ResponseEntity<ErrorGenerico> handleCustomNotFoundException(CustomNotFoundException exception) {
        ApiException apiException = new ApiException(
                TypeMessage.DANGER,
                exception.getMessage(),
                HttpStatus.NOT_FOUND
        );
        LOGGER.warn(exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiException.getErrorGenerico());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorGenerico> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {
        List<ErrorCampo> errores = new ArrayList<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            ErrorCampo errorCampo = new ErrorCampo();
            errorCampo.setCampo(((FieldError) error).getField());
            errorCampo.setMensaje(error.getDefaultMessage());
            errores.add(errorCampo);
        });

        ApiException apiException = new ApiException(
                TypeMessage.DANGER,
                "Errores de validación",
                HttpStatus.BAD_REQUEST,
                errores
        );
        LOGGER.warn(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiException.getErrorGenerico());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorGenerico> handleConstraintViolationException(
            ConstraintViolationException exception) {
        ApiException apiException = new ApiException(
                TypeMessage.DANGER,
                exception.getMessage(),
                HttpStatus.BAD_REQUEST
        );
        LOGGER.warn(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiException.getErrorGenerico());
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Object> handleSQLException(
            SQLIntegrityConstraintViolationException exception) {
        LOGGER.warn("Database uniqueness or integrity constraint rejected a request");
        return MessageResponse.setResponse(false, HttpStatus.CONFLICT,
                "El registro entra en conflicto con un valor único o una relación existente");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(
            DataIntegrityViolationException exception) {
        LOGGER.warn("Database uniqueness or integrity constraint rejected a request");
        return MessageResponse.setResponse(false, HttpStatus.CONFLICT,
                "El registro entra en conflicto con un valor único o una relación existente");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorGenerico> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException exception) {
        ApiException apiException = new ApiException(
                TypeMessage.DANGER,
                exception.getMessage(),
                HttpStatus.BAD_REQUEST
        );
        LOGGER.warn(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiException.getErrorGenerico());
    }
}
