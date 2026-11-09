package com.agrihusac.SysInvent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.agrihusac.SysInvent.api.LoginController;
import com.agrihusac.SysInvent.model.request.LoginRequest;
import com.agrihusac.SysInvent.model.response.auth.LoginResponse;
import com.agrihusac.SysInvent.service.LoginService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class LoginIntegrationTest {

    @InjectMocks
    private LoginController loginController;

    @Mock
    private LoginService loginService;

    @Test
    void loginConCredencialesValidasDevuelveToken() {
        LoginRequest request = new LoginRequest("admin-login", "Secret123!");
        LoginResponse esperado = new LoginResponse(
                true, "Inicio de sesion correcto", HttpStatus.OK.value(), "token-simulado");
        when(loginService.login(request)).thenReturn(ResponseEntity.ok(esperado));

        ResponseEntity<LoginResponse> resultado = loginController.login(request);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(true, resultado.getBody().success());
        assertEquals("token-simulado", resultado.getBody().data());
        verify(loginService).login(request);
    }

    @Test
    void loginConCredencialesInvalidasDevuelveUnauthorized() {
        LoginRequest request = new LoginRequest("admin-login", "incorrecta");
        LoginResponse esperado = new LoginResponse(
                false, "Credenciales inválidas", HttpStatus.UNAUTHORIZED.value(), null);
        when(loginService.login(request))
                .thenReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(esperado));

        ResponseEntity<LoginResponse> resultado = loginController.login(request);

        assertEquals(HttpStatus.UNAUTHORIZED, resultado.getStatusCode());
        assertEquals(false, resultado.getBody().success());
        verify(loginService).login(request);
    }
}
