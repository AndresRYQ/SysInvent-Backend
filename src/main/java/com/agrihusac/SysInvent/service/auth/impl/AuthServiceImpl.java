package com.agrihusac.SysInvent.service.auth.impl;

import com.agrihusac.SysInvent.model.request.LoginRequest;
import com.agrihusac.SysInvent.service.LoginService;
import com.agrihusac.SysInvent.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final LoginService loginService;

    @Override
    public ResponseEntity<Object> login(LoginRequest request) {
        return loginService.login(request);
    }
}
