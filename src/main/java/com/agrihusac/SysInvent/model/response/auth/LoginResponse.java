package com.agrihusac.SysInvent.model.response.auth;

/**
 * Typed response returned by the login endpoint.
 */
public record LoginResponse(boolean success, String message, int status, String data) {
}
