package com.agrihusac.SysInvent.service;

import com.agrihusac.SysInvent.model.request.LoginRequest;
import org.springframework.http.ResponseEntity;

public interface LoginService {

    ResponseEntity<Object> login(LoginRequest request);
}
