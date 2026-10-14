package com.agrihusac.SysInvent.utils;

import jakarta.validation.ConstraintViolation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class MessageResponse {

    private final boolean success;
    private final String message;
    private final int status;
    private final Object data;

    private MessageResponse() {
        this(false, null, 0, null);
    }

    private MessageResponse(boolean success, String message, int status, Object data) {
        this.success = success;
        this.message = message;
        this.status = status;
        this.data = data;
    }

    public static ResponseEntity<MessageResponse> setMessageResponse(
            boolean success,
            HttpStatus status,
            String message) {
        return new ResponseEntity<>(
                new MessageResponse(success, message, status.value(), null),
                status);
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
