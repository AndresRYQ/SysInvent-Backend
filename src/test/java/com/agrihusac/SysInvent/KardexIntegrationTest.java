package com.agrihusac.SysInvent;

import com.agrihusac.SysInvent.model.entity.ContactoEntity;
import com.agrihusac.SysInvent.model.entity.ProductoEntity;
import com.agrihusac.SysInvent.model.entity.ProveedorEntity;
import com.agrihusac.SysInvent.model.entity.TipoDocumentoEntity;
import com.agrihusac.SysInvent.repository.ContactoRepository;
import com.agrihusac.SysInvent.repository.ProductoRepository;
import com.agrihusac.SysInvent.repository.ProveedorRepository;
import com.agrihusac.SysInvent.repository.TipoDocumentoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.agrihusac.SysInvent.model.entity.CategoriaEntity;
import com.agrihusac.SysInvent.model.entity.TipoProductoEntity;
import com.agrihusac.SysInvent.model.entity.UnidadMedidaEntity;
import com.agrihusac.SysInvent.repository.CategoriaRepository;
import com.agrihusac.SysInvent.repository.TipoProductoRepository;
import com.agrihusac.SysInvent.repository.UnidadMedidaRepository;
import org.springframework.test.context.ActiveProfiles;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import com.agrihusac.SysInvent.model.entity.KardexMovimientoEntity;
import com.agrihusac.SysInvent.repository.KardexMovimientoRepository;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(
        username = "warehouse-user",
        authorities = {
                "MODULE_/ingresos-almacen",
                "MODULE_/control-almacen"
        }
)
class KardexIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Autowired
    private ContactoRepository contactoRepository;

    @Autowired
    private TipoDocumentoRepository tipoDocumentoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private TipoProductoRepository tipoProductoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UnidadMedidaRepository unidadMedidaRepository;

    @Autowired
    private KardexMovimientoRepository kardexMovimientoRepository;

    private Integer proveedorId;
    private Integer contactoId;
    private Integer tipoDocumentoId;
    private Integer productoId;

    @BeforeEach
    void prepararCatalogos() {
        String sufijo = UUID.randomUUID().toString();

        ProveedorEntity proveedor = new ProveedorEntity();
        proveedor.setRuc(sufijo.replace("-", "").substring(0, 11));
        proveedor.setRazonSocial("Proveedor " + sufijo);
        proveedor.setActivo(true);
        proveedor = proveedorRepository.save(proveedor);
        proveedorId = proveedor.getProveedorId();

        ContactoEntity contacto = new ContactoEntity();
        contacto.setProveedor(proveedor);
        contacto.setNombreCompleto("Contacto " + sufijo);
        contacto.setActivo(true);
        contactoId = contactoRepository.save(contacto).getContactoId();

        TipoDocumentoEntity tipoDocumento = new TipoDocumentoEntity();
        tipoDocumento.setNombre("Documento " + sufijo);
        tipoDocumento.setActivo(true);
        tipoDocumentoId = tipoDocumentoRepository.save(tipoDocumento).getTipoDocumentoId();

        TipoProductoEntity tipoProducto = new TipoProductoEntity();
        tipoProducto.setNombre("Tipo " + sufijo);
        tipoProducto.setActivo(true);
        Integer tipoProductoId =
                tipoProductoRepository.save(tipoProducto).getTipoProductoId();

        CategoriaEntity categoria = new CategoriaEntity();
        categoria.setNombre("Categoría " + sufijo);
        categoria.setActivo(true);
        Integer categoriaId =
                categoriaRepository.save(categoria).getCategoriaId();

        UnidadMedidaEntity unidadMedida = new UnidadMedidaEntity();
        unidadMedida.setNombre("Unidad " + sufijo);
        unidadMedida.setActivo(true);
        Integer unidadMedidaId =
                unidadMedidaRepository.save(unidadMedida).getUnidadMedidaId();

        ProductoEntity producto = new ProductoEntity();
        producto.setActivo(true);
        producto.setCodigo(sufijo);
        producto.setNombre("Producto " + sufijo);
        producto.setTipoProductoId(tipoProductoId);
        producto.setCategoriaId(categoriaId);
        producto.setUnidadMedidaId(unidadMedidaId);
        producto.setProveedorId(proveedorId);
        producto.setStockActual(new BigDecimal("0.0000"));
        producto.setStockMinimo(new BigDecimal("5.0000"));
        producto.setPrecioUnitario(new BigDecimal("1.0000"));
        productoId = productoRepository.save(producto).getProductoId();
    }

    @Test
    void ingresoActualizaStockYQuedaConsultableEnKardex() throws Exception {
        String payload = """
                {
                  "fechaIngreso":"%s",
                  "proveedorId":%d,
                  "contactoId":%d,
                  "tipoDocumentoId":%d,
                  "numeroDocumento":"FAC-123",
                  "observacion":"Recepción de prueba",
                  "detalles":[{"productoId":%d,"cantidad":3.0000,"precioUnitario":2.5000}]
                }
                """.formatted(LocalDate.now(), proveedorId, contactoId, tipoDocumentoId, productoId);

        String response = mockMvc.perform(post("/api/ingresos-almacen")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.estado").value("REGISTRADO"))
                .andExpect(jsonPath("$.data.detalles[0].stockResultante").value(3))
                .andReturn().getResponse().getContentAsString();

        int ingresoId = objectMapper.readTree(response).path("data").path("id").asInt();
        BigDecimal stock = productoRepository.findById(productoId).orElseThrow().getStockActual();
        assertEquals(0, stock.compareTo(new BigDecimal("3.0000")));

        mockMvc.perform(get("/api/kardex/movimientos")
                        .param("productoId", productoId.toString())
                        .param("desde", LocalDate.now().toString())
                        .param("hasta", LocalDate.now().toString())
                        .param("tipoMovimiento", "ENTRADA_INGRESO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].ingresoAlmacenId").value(ingresoId))
                .andExpect(jsonPath("$.data[0].usuarioResponsable").value("warehouse-user"))
                .andExpect(jsonPath("$.data[0].saldoNuevo").value(3));

        mockMvc.perform(get("/api/kardex/stock").param("soloMinimo", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[?(@.productoId == %d)].requiereReposicion".formatted(productoId))
                        .value(hasItem(true)));
    }

    @Test
    void anulacionMantieneTrazabilidadPeroNoAfectaTotalesNetos()
            throws Exception {
        ProductoEntity producto =
                productoRepository.findById(productoId).orElseThrow();

        producto.setStockActual(new BigDecimal("2.0000"));
        productoRepository.saveAndFlush(producto);

        LocalDateTime fechaMovimiento = LocalDateTime.now();

        KardexMovimientoEntity salida =
                new KardexMovimientoEntity();

        salida.setUsuarioResponsable("warehouse-user");
        salida.setProductoId(productoId);
        salida.setTipoMovimiento("SALIDA_VALE");
        salida.setFechaMovimiento(fechaMovimiento.minusSeconds(1));
        salida.setCantidad(new BigDecimal("2.0000"));
        salida.setSaldoAnterior(new BigDecimal("2.0000"));
        salida.setSaldoNuevo(BigDecimal.ZERO);
        salida.setPrecioUnitario(new BigDecimal("1.0000"));
        salida.setNumeroDocumento("VAL-ANULADO-001");

        kardexMovimientoRepository.saveAndFlush(salida);

        KardexMovimientoEntity anulacion =
                new KardexMovimientoEntity();

        anulacion.setUsuarioResponsable("warehouse-user");
        anulacion.setProductoId(productoId);
        anulacion.setTipoMovimiento("ANULACION_ENTRADA");
        anulacion.setFechaMovimiento(fechaMovimiento);
        anulacion.setCantidad(new BigDecimal("2.0000"));
        anulacion.setSaldoAnterior(BigDecimal.ZERO);
        anulacion.setSaldoNuevo(new BigDecimal("2.0000"));
        anulacion.setPrecioUnitario(new BigDecimal("1.0000"));
        anulacion.setNumeroDocumento("VAL-ANULADO-001");

        kardexMovimientoRepository.saveAndFlush(anulacion);

        mockMvc.perform(
                        get("/api/almacen/existencias")
                                .param("busqueda", producto.getCodigo())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.datos[0].entradas").value(0))
                .andExpect(jsonPath("$.datos[0].salidas").value(0))
                .andExpect(jsonPath("$.datos[0].stock").value(2));

        mockMvc.perform(
                        get(
                                "/api/almacen/existencias/{productoId}/detalle",
                                productoId
                        )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.datos[0].movimiento").value("Salida"))
                .andExpect(jsonPath("$.datos[1].movimiento").value("Entrada"))
                .andExpect(jsonPath("$.datos[1].documento")
                        .value("VAL-ANULADO-001"));
    }

    @Test
    void fechaDesdeNoPuedeSerPosteriorAHasta() throws Exception {
        mockMvc.perform(get("/api/kardex/movimientos")
                        .param("desde", "2026-10-16")
                        .param("hasta", "2026-10-15"))
                .andExpect(status().isBadRequest());
    }
}