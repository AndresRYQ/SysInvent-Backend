package com.agrihusac.SysInvent;

import com.agrihusac.SysInvent.model.entity.DestinoEntity;
import com.agrihusac.SysInvent.model.response.destino.DestinoResponse;
import com.agrihusac.SysInvent.repository.DestinoRepository;
import com.agrihusac.SysInvent.service.destino.impl.DestinoServiceImpl;
import com.agrihusac.SysInvent.utils.CustomPage;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CatalogoEstadoServiceTest {

    @Mock
    private DestinoRepository destinoRepository;

    private DestinoServiceImpl destinoService;

    @BeforeEach
    void configurar() {
        destinoService =
                new DestinoServiceImpl(
                        destinoRepository
                );
    }

    @Test
    void listaSolamenteRegistrosInactivosCuandoSeSolicita() {
        Pageable pageable =
                PageRequest.of(0, 10);

        DestinoEntity destino =
                new DestinoEntity();

        destino.setDestinoId(5);
        destino.setNombre("Destino inactivo");
        destino.setDescripcion("Registro de prueba");
        destino.setActivo(false);

        when(destinoRepository
                .findByActivoAndNombreContainingIgnoreCase(
                        false,
                        "",
                        pageable
                ))
                .thenReturn(
                        new PageImpl<>(
                                List.of(destino),
                                pageable,
                                1
                        )
                );

        CustomPage<DestinoResponse> resultado =
                destinoService.listar(
                        "",
                        false,
                        pageable
                );

        assertEquals(
                1,
                resultado.getDatos().size()
        );

        assertFalse(
                resultado.getDatos()
                        .get(0)
                        .getActivo()
        );

        verify(destinoRepository)
                .findByActivoAndNombreContainingIgnoreCase(
                        false,
                        "",
                        pageable
                );
    }

    @Test
    void listaTodosLosEstadosCuandoNoSeEnviaFiltro() {
        Pageable pageable =
                PageRequest.of(0, 10);

        when(destinoRepository
                .findByNombreContainingIgnoreCase(
                        "",
                        pageable
                ))
                .thenReturn(
                        new PageImpl<>(
                                List.of(),
                                pageable,
                                0
                        )
                );

        destinoService.listar(
                "",
                null,
                pageable
        );

        verify(destinoRepository)
                .findByNombreContainingIgnoreCase(
                        "",
                        pageable
                );
    }
}