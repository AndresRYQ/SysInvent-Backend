package com.agrihusac.SysInvent;

import com.agrihusac.SysInvent.model.entity.CentroCostoEntity;
import com.agrihusac.SysInvent.model.entity.CategoriaEntity;
import com.agrihusac.SysInvent.model.entity.DestinoEntity;
import com.agrihusac.SysInvent.model.entity.ProductoEntity;
import com.agrihusac.SysInvent.model.entity.ProveedorEntity;
import com.agrihusac.SysInvent.model.entity.TipoProductoEntity;
import com.agrihusac.SysInvent.model.entity.UnidadMedidaEntity;
import com.agrihusac.SysInvent.repository.CategoriaRepository;
import com.agrihusac.SysInvent.repository.CentroCostoRepository;
import com.agrihusac.SysInvent.repository.DestinoRepository;
import com.agrihusac.SysInvent.repository.ProductoRepository;
import com.agrihusac.SysInvent.repository.ProveedorRepository;
import com.agrihusac.SysInvent.repository.TipoProductoRepository;
import com.agrihusac.SysInvent.repository.UnidadMedidaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test-user")
class ValeConsumoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CentroCostoRepository centroCostoRepository;

    @Autowired
    private DestinoRepository destinoRepository;

    @Autowired
    private TipoProductoRepository tipoProductoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UnidadMedidaRepository unidadMedidaRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Autowired
    private ProductoRepository productoRepository;

    private Integer centroCostoId;
    private Integer destinoId;
    private Integer productoId;

    @Test
    @WithAnonymousUser
    void voucherRoutesRequireAuthentication() throws Exception {
        mockMvc.perform(get("/api/vales-consumo"))
                .andExpect(status().isUnauthorized());
    }

    @BeforeEach
    void prepararCatalogos() {
        String sufijo = UUID.randomUUID().toString();

        CentroCostoEntity centroCosto = new CentroCostoEntity();
        centroCosto.setNombre("Centro " + sufijo);
        centroCosto.setActivo(true);
        centroCostoId = centroCostoRepository.save(centroCosto).getCentroCostoId();

        DestinoEntity destino = new DestinoEntity();
        destino.setNombre("Destino " + sufijo);
        destino.setActivo(true);
        destinoId = destinoRepository.save(destino).getDestinoId();

        TipoProductoEntity tipoProducto = new TipoProductoEntity();
        tipoProducto.setNombre("Tipo " + sufijo);
        tipoProducto.setActivo(true);
        tipoProducto = tipoProductoRepository.save(tipoProducto);

        CategoriaEntity categoria = new CategoriaEntity();
        categoria.setNombre("Categoria " + sufijo);
        categoria.setActivo(true);
        categoria = categoriaRepository.save(categoria);

        ProveedorEntity proveedor = new ProveedorEntity();
        proveedor.setRuc(UUID.randomUUID().toString().replace("-", "").substring(0, 11));
        proveedor.setRazonSocial("Proveedor " + sufijo);
        proveedor.setActivo(true);
        proveedor = proveedorRepository.save(proveedor);

        UnidadMedidaEntity unidad = new UnidadMedidaEntity();
        unidad.setNombre("Unidad " + sufijo);
        unidad.setActivo(true);
        unidad = unidadMedidaRepository.save(unidad);

        ProductoEntity producto = new ProductoEntity();
        producto.setActivo(true);
        producto.setCodigo(sufijo);
        producto.setNombre("Producto " + sufijo);
        producto.setTipoProducto(tipoProducto);
        producto.setCategoria(categoria);
        producto.setUnidadMedida(unidad);
        producto.setProveedor(proveedor);
        producto.setStockActual(new BigDecimal("5.0000"));
        producto.setStockMinimo(BigDecimal.ZERO);
        producto.setPrecioUnitario(new BigDecimal("10.0000"));
        productoId = productoRepository.save(producto).getProductoId();
    }

    @Test
    void createUpdateAndCancelAdjustStockAndRegisterKardex() throws Exception {
        String createResponse = mockMvc.perform(post("/api/vales-consumo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload("2.0000")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.estado").value("REGISTRADO"))
                .andExpect(jsonPath("$.data.movimientosKardex[0].tipoMovimiento").value("SALIDA_VALE"))
                .andExpect(jsonPath("$.data.movimientosKardex[0].usuarioResponsable").value("test-user"))
                .andReturn().getResponse().getContentAsString();

        int valeId = objectMapper.readTree(createResponse).path("data").path("id").asInt();
        assertStock("3.0000");

        mockMvc.perform(get("/api/vales-consumo/{id}", valeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.numeroVale").isNotEmpty())
                .andExpect(jsonPath("$.data.detalles[0].cantidad").value(2));

        mockMvc.perform(put("/api/vales-consumo/{id}", valeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload("4.0000")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.movimientosKardex[1].tipoMovimiento").value("AJUSTE_SALIDA"));
        assertStock("1.0000");

        mockMvc.perform(put("/api/vales-consumo/{id}", valeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload("1.5000")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.movimientosKardex[2].tipoMovimiento").value("AJUSTE_ENTRADA"));
        assertStock("3.5000");

        mockMvc.perform(patch("/api/vales-consumo/{id}/anular", valeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.estado").value("ANULADO"))
                .andExpect(jsonPath("$.data.movimientosKardex[3].tipoMovimiento").value("ANULACION_ENTRADA"));
        assertStock("5.0000");

        mockMvc.perform(patch("/api/vales-consumo/{id}/anular", valeId))
                .andExpect(status().isConflict());
        mockMvc.perform(put("/api/vales-consumo/{id}", valeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload("1.0000")))
                .andExpect(status().isConflict());
    }

    @Test
    void insufficientStockReturnsConflictAndDoesNotCreateVoucher() throws Exception {
        mockMvc.perform(post("/api/vales-consumo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload("6.0000")))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.data.code").value("STOCK_INSUFFICIENT"))
                .andExpect(jsonPath("$.data.productos[0].cantidadSolicitada").value(6));
        assertStock("5.0000");

        mockMvc.perform(get("/api/vales-consumo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.datos").isArray());
    }

    private String payload(String cantidad) {
        return """
                {
                  "fechaVale":"2026-10-13",
                  "centroCostoId":%d,
                  "solicitante":"Solicitante de prueba",
                  "motivo":"Prueba de consumo",
                  "detalles":[{
                    "productoId":%d,
                    "distribuciones":[{
                      "destinoId":%d,
                      "parteEquipoId":null,
                      "cantidad":%s
                    }]
                  }]
                }
                """.formatted(centroCostoId, productoId, destinoId, cantidad);
    }

    private void assertStock(String esperado) {
        BigDecimal actual = productoRepository.findById(productoId).orElseThrow().getStockActual();
        org.junit.jupiter.api.Assertions.assertEquals(0, actual.compareTo(new BigDecimal(esperado)));
    }
}
