package com.agrihusac.SysInvent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.agrihusac.SysInvent.api.KardexController;
import com.agrihusac.SysInvent.service.reporte.KardexService;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class KardexIntegrationTest {

    @InjectMocks
    private KardexController kardexController;

    @Mock
    private KardexService kardexService;

    @Test
    void consultaStockMedianteServicioMockeado() {
        when(kardexService.consultarStock(true)).thenReturn(List.of());

        ResponseEntity<Object> resultado = kardexController.stock(true);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        verify(kardexService).consultarStock(true);
    }

    @Test
    void fechaDesdeNoPuedeSerPosteriorAHasta() {
        ResponseStatusException excepcion = assertThrows(ResponseStatusException.class,
                () -> kardexController.movimientos(null,
                        LocalDate.of(2026, 10, 16), LocalDate.of(2026, 10, 15), null));

        assertEquals(HttpStatus.BAD_REQUEST, excepcion.getStatusCode());
    }
}
