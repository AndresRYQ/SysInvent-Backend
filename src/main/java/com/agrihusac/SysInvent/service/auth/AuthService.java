package com.agrihusac.SysInvent.service.auth;

import com.agrihusac.SysInvent.model.request.LoginRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<Object> login(LoginRequest request);
}
