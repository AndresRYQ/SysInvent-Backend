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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "warehouse-user")
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

        ProductoEntity producto = new ProductoEntity();
        producto.setActivo(true);
        producto.setCodigo(sufijo);
        producto.setNombre("Producto " + sufijo);
        producto.setTipoProductoId(1);
        producto.setCategoriaId(1);
        producto.setUnidadMedidaId(1);
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
    void fechaDesdeNoPuedeSerPosteriorAHasta() throws Exception {
        mockMvc.perform(get("/api/kardex/movimientos")
                        .param("desde", "2026-10-16")
                        .param("hasta", "2026-10-15"))
                .andExpect(status().isBadRequest());
    }
}