package com.agrihusac.SysInvent.model.response.auth;

/**
 * Describes the response currently produced by MessageResponse for a successful login.
 * Existing login controller responses are not changed to use this DTO.
 */
public record LoginResponse(boolean success, String message, int status, String data) {
}
