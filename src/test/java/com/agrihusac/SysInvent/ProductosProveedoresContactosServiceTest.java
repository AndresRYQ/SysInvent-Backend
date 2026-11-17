package com.agrihusac.SysInvent;

import com.agrihusac.SysInvent.model.entity.ProveedorEntity;
import com.agrihusac.SysInvent.model.request.contacto.ContactoRequest;
import com.agrihusac.SysInvent.repository.CategoriaRepository;
import com.agrihusac.SysInvent.repository.ContactoRepository;
import com.agrihusac.SysInvent.repository.ProductoRepository;
import com.agrihusac.SysInvent.repository.ProveedorRepository;
import com.agrihusac.SysInvent.repository.TipoProductoRepository;
import com.agrihusac.SysInvent.repository.UnidadMedidaRepository;
import com.agrihusac.SysInvent.service.contacto.impl.ContactoServiceImpl;
import com.agrihusac.SysInvent.service.producto.impl.ProductoServiceImpl;
import com.agrihusac.SysInvent.service.proveedor.impl.ProveedorServiceImpl;
import com.agrihusac.SysInvent.utils.CustomPage;
import com.agrihusac.SysInvent.utils.MessageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductosProveedoresContactosServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private ProveedorRepository proveedorRepository;

    @Mock
    private ContactoRepository contactoRepository;

    @Mock
    private TipoProductoRepository tipoProductoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private UnidadMedidaRepository unidadMedidaRepository;

    private ProductoServiceImpl productoService;
    private ProveedorServiceImpl proveedorService;
    private ContactoServiceImpl contactoService;

    @BeforeEach
    void configurarServicios() {
        productoService = new ProductoServiceImpl(
                productoRepository,
                tipoProductoRepository,
                categoriaRepository,
                unidadMedidaRepository,
                proveedorRepository
        );

        proveedorService =
                new ProveedorServiceImpl(proveedorRepository);

        contactoService = new ContactoServiceImpl(
                contactoRepository,
                proveedorRepository
        );
    }

    @Test
    void productosAplicaFiltrosAntesDePaginar() {
        Pageable pageable = PageRequest.of(0, 10);

        when(productoRepository.buscarProductosCatalogo(
                "tornillo",
                2,
                3,
                false,
                pageable
        )).thenReturn(Page.empty(pageable));

        CustomPage<?> resultado = productoService.listar(
                " tornillo ",
                2,
                3,
                false,
                pageable
        );

        assertEquals(
                0L,
                resultado.getPaginacion().getTotalElementos()
        );

        verify(productoRepository).buscarProductosCatalogo(
                "tornillo",
                2,
                3,
                false,
                pageable
        );
    }

    @Test
    void proveedoresAplicaEstadoAntesDePaginar() {
        Pageable pageable = PageRequest.of(0, 10);

        when(proveedorRepository
                .findByActivoAndRazonSocialContainingIgnoreCase(
                        false,
                        "acme",
                        pageable
                ))
                .thenReturn(Page.empty(pageable));

        CustomPage<?> resultado = proveedorService.listar(
                " acme ",
                false,
                pageable
        );

        assertEquals(
                0L,
                resultado.getPaginacion().getTotalElementos()
        );

        verify(proveedorRepository)
                .findByActivoAndRazonSocialContainingIgnoreCase(
                        false,
                        "acme",
                        pageable
                );
    }

    @Test
    void contactoDuplicadoEnElMismoProveedorDevuelveConflicto() {
        ProveedorEntity proveedor = new ProveedorEntity();
        proveedor.setActivo(true);

        when(proveedorRepository.findById(7))
                .thenReturn(Optional.of(proveedor));
        when(contactoRepository
                .existsByProveedorProveedorIdAndNombreCompletoIgnoreCase(
                        7,
                        "Ana Torres"
                ))
                .thenReturn(true);

        ResponseEntity<MessageResponse> resultado =
                contactoService.crear(new ContactoRequest(
                        7,
                        " Ana Torres ",
                        "Compras",
                        "987654321",
                        "ana@example.com"
                ));

        assertEquals(HttpStatus.CONFLICT, resultado.getStatusCode());
        verify(contactoRepository, never()).saveAndFlush(any());
    }

    @Test
    void contactosAplicaProveedorYEstadoAntesDePaginar() {
        Pageable pageable = PageRequest.of(0, 10);

        when(contactoRepository.buscarContactos(
                "ana",
                7,
                true,
                pageable
        )).thenReturn(Page.empty(pageable));

        CustomPage<?> resultado = contactoService.listar(
                " ana ",
                7,
                true,
                pageable
        );

        assertEquals(
                0L,
                resultado.getPaginacion().getTotalElementos()
        );

        verify(contactoRepository).buscarContactos(
                "ana",
                7,
                true,
                pageable
        );
    }
}