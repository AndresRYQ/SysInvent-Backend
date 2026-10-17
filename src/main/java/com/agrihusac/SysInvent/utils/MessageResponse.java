package com.agrihusac.SysInvent.utils;

import jakarta.validation.ConstraintViolation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;

import java.util.Set;
import java.util.stream.Collectors;

public class MessageResponse {

    private final boolean success;
    private final String message;
    private final int status;
    private final Object data;

    public MessageResponse(boolean success, String message, int status, Object data) {
        this.success = success;
        this.message = message;
        this.status = status;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public Object getData() {
        return data;
    }

    @SuppressWarnings("unchecked")
    public static <T> ResponseEntity<T> setResponse(
            @Nullable boolean success,
            HttpStatus status,
            @Nullable String message,
            @Nullable Object data) {
        MessageResponse response = new MessageResponse(success, message, status.value(), data);
        return (ResponseEntity<T>) new ResponseEntity<>(response, status);
    }

    public static <T> ResponseEntity<T> setResponse(
            HttpStatus status,
            String message,
            @Nullable Object data) {
        return setResponse(false, status, message, data);
    }

    public static <T> ResponseEntity<T> setResponse(
            boolean success,
            HttpStatus status,
            String message) {
        return setResponse(success, status, message, null);
    }

    public static <T> ResponseEntity<T> setResponse(HttpStatus status, String message) {
        return setResponse(false, status, message, null);
    }

    public static <T> ResponseEntity<T> setResponse(
            boolean success,
            String message,
            @Nullable Object data) {
        HttpStatus status = success ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return setResponse(success, status, message, data);
    }

    public static <T> ResponseEntity<T> setResponse(boolean success, String message) {
        return setResponse(success, message, null);
    }

    public static <T> ResponseEntity<T> setResponse(
            Set<? extends ConstraintViolation<?>> violations) {
        String message = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        return setResponse(false, HttpStatus.BAD_REQUEST, message, null);
    }
}
