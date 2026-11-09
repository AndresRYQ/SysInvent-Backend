package com.agrihusac.SysInvent;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.agrihusac.SysInvent.api.IngresoAlmacenController;
import com.agrihusac.SysInvent.service.ingreso.IngresoAlmacenService;
import com.agrihusac.SysInvent.utils.MessageResponse;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class IngresoAlmacenIntegrationTest {

    @InjectMocks
    private IngresoAlmacenController ingresoAlmacenController;

    @Mock
    private IngresoAlmacenService ingresoAlmacenService;

    @Test
    void obtieneIngresoMedianteServicioMockeado() {
        ResponseEntity<Object> esperado = MessageResponse.setResponse(
                true, HttpStatus.OK, "Ingreso encontrado", Map.of("id", 1));
        when(ingresoAlmacenService.obtener(1)).thenReturn(esperado);

        ResponseEntity<Object> resultado = ingresoAlmacenController.obtener(1);

        assertSame(esperado, resultado);
        verify(ingresoAlmacenService).obtener(1);
    }
}
