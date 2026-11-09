package com.agrihusac.SysInvent;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.agrihusac.SysInvent.api.RolController;
import com.agrihusac.SysInvent.service.RolService;
import com.agrihusac.SysInvent.utils.MessageResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class RolesSecurityIntegrationTest {

    @InjectMocks
    private RolController rolController;

    @Mock
    private RolService rolService;

    @Test
    void actualizaEstadoDelRolMedianteServicioMockeado() {
        ResponseEntity<MessageResponse> esperado = ResponseEntity.ok(
                new MessageResponse(true, "Rol actualizado", 200, null));
        when(rolService.actualizarEstadoRol(1, false)).thenReturn(esperado);

        ResponseEntity<MessageResponse> resultado = rolController.actualizarEstadoRol(1, false);

        assertSame(esperado, resultado);
        verify(rolService).actualizarEstadoRol(1, false);
    }
}
