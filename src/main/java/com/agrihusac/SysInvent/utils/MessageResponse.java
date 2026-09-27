package com.agrihusac.SysInvent.utils;

import jakarta.validation.ConstraintViolation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MessageResponse {

    private MessageResponse() {
    }

    public static ResponseEntity<Object> setResponse(
            @Nullable boolean success,
            HttpStatus status,
            @Nullable String message,
            @Nullable Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", message);
        response.put("status", status.value());
        response.put("data", data);
        return new ResponseEntity<>(response, status);
    }

    public static ResponseEntity<Object> setResponse(
            HttpStatus status,
            String message,
            @Nullable Object data) {
        return setResponse(false, status, message, data);
    }

    public static ResponseEntity<Object> setResponse(
            boolean success,
            HttpStatus status,
            String message) {
        return setResponse(success, status, message, null);
    }

    public static ResponseEntity<Object> setResponse(HttpStatus status, String message) {
        return setResponse(false, status, message, null);
    }

    public static ResponseEntity<Object> setResponse(
            boolean success,
            String message,
            @Nullable Object data) {
        HttpStatus status = success ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return setResponse(success, status, message, data);
    }

    public static ResponseEntity<Object> setResponse(boolean success, String message) {
        return setResponse(success, message, null);
    }

    public static ResponseEntity<Object> setResponse(
            Set<? extends ConstraintViolation<?>> violations) {
        String message = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        return setResponse(false, HttpStatus.BAD_REQUEST, message, null);
    }
}
