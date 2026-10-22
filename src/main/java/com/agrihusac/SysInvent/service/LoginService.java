package com.agrihusac.SysInvent.service;

import com.agrihusac.SysInvent.model.request.LoginRequest;
import com.agrihusac.SysInvent.model.response.auth.LoginResponse;
import org.springframework.http.ResponseEntity;

public interface LoginService {

    ResponseEntity<LoginResponse> login(LoginRequest request);
}
