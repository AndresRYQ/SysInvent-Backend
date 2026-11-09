package com.agrihusac.SysInvent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.agrihusac.SysInvent.api.ValeConsumoController;
import com.agrihusac.SysInvent.service.valeconsumo.ValeConsumoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class ValeConsumoIntegrationTest {

    @InjectMocks
    private ValeConsumoController valeConsumoController;

    @Mock
    private ValeConsumoService valeConsumoService;

    @Test
    void obtieneValeMedianteServicioMockeado() {
        when(valeConsumoService.obtener(1)).thenReturn(null);

        ResponseEntity<Object> resultado = valeConsumoController.obtener(1);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        verify(valeConsumoService).obtener(1);
    }
}
