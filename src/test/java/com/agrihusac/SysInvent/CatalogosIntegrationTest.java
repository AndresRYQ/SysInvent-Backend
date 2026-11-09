package com.agrihusac.SysInvent;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.agrihusac.SysInvent.api.ProveedorController;
import com.agrihusac.SysInvent.model.request.proveedor.ProveedorRequest;
import com.agrihusac.SysInvent.service.proveedor.ProveedorService;
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
class CatalogosIntegrationTest {

    @InjectMocks
    private ProveedorController proveedorController;

    @Mock
    private ProveedorService proveedorService;

    @Test
    void registraProveedorConRucValidoMedianteServicioMockeado() {
        ProveedorRequest request = new ProveedorRequest(
                "20123456789", "Proveedor de prueba", null, null, null);
        ResponseEntity<MessageResponse> esperado = MessageResponse.setResponse(
                true, HttpStatus.CREATED, "Proveedor registrado", Map.of("id", 1));
        when(proveedorService.crear(request)).thenReturn(esperado);

        ResponseEntity<MessageResponse> resultado = proveedorController.registrarProveedor(request);

        assertSame(esperado, resultado);
        verify(proveedorService).crear(request);
    }
}
