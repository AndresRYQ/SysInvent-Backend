package com.agrihusac.SysInvent;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CatalogosIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void categorySupportsCreateListUpdateDeactivateAndReactivate() throws Exception {
        String name = "Categoria-" + UUID.randomUUID();
        String created = mockMvc.perform(post("/api/v1/catalogos/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre":"%s","descripcion":"Descripción"}
                                """.formatted(name)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.nombre").value(name))
                .andExpect(jsonPath("$.data.activo").value(true))
                .andReturn().getResponse().getContentAsString();
        int id = objectMapper.readTree(created).path("data").path("id").asInt();

        mockMvc.perform(get("/api/v1/catalogos/categorias").param("nombre", name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.datos[0].id").value(id));

        mockMvc.perform(put("/api/v1/catalogos/categorias/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre":"%s","descripcion":"Actualizada"}
                                """.formatted(name)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.descripcion").value("Actualizada"));

        mockMvc.perform(delete("/api/v1/catalogos/categorias/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.activo").value(false));
        mockMvc.perform(get("/api/v1/catalogos/categorias").param("nombre", name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.datos").isEmpty());

        mockMvc.perform(put("/api/v1/catalogos/categorias/{id}/estado", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"activo\":true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.activo").value(true));
    }

    @Test
    void rejectsInvalidAndDuplicateCatalogNames() throws Exception {
        mockMvc.perform(post("/api/v1/catalogos/tipos-producto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\" \",\"descripcion\":\"inválido\"}"))
                .andExpect(status().isBadRequest());

        String name = "Tipo-" + UUID.randomUUID();
        String payload = objectMapper.createObjectNode()
                .put("nombre", name)
                .put("descripcion", "prueba").toString();
        mockMvc.perform(post("/api/v1/catalogos/tipos-producto")
                        .contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/api/v1/catalogos/tipos-producto")
                        .contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isConflict());

        String partCode = "P" + UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        String partPayload = """
                {"codigo":"%s","nombre":"Parte de prueba"}
                """.formatted(partCode);
        mockMvc.perform(post("/api/v1/catalogos/partes-equipo")
                        .contentType(MediaType.APPLICATION_JSON).content(partPayload))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/api/v1/catalogos/partes-equipo")
                        .contentType(MediaType.APPLICATION_JSON).content(partPayload))
                .andExpect(status().isConflict());
    }

    @Test
    void supplierRucIsUniqueAndContactRetainsSupplierRelationship() throws Exception {
        String ruc = UUID.randomUUID().toString().replace("-", "").substring(0, 11);
        String supplierPayload = """
                {"ruc":"%s","razonSocial":"Proveedor test","correo":"proveedor@example.test"}
                """.formatted(ruc);
        String created = mockMvc.perform(post("/api/v1/catalogos/proveedores")
                        .contentType(MediaType.APPLICATION_JSON).content(supplierPayload))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        int proveedorId = objectMapper.readTree(created).path("data").path("proveedorId").asInt();

        mockMvc.perform(post("/api/v1/catalogos/proveedores")
                        .contentType(MediaType.APPLICATION_JSON).content(supplierPayload))
                .andExpect(status().isConflict());

        String contactPayload = """
                {"proveedorId":%d,"nombreCompleto":"Contacto prueba","cargo":"Compras","correo":"contacto@example.test"}
                """.formatted(proveedorId);
        mockMvc.perform(post("/api/v1/catalogos/contactos")
                        .contentType(MediaType.APPLICATION_JSON).content(contactPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.proveedorId").value(proveedorId));
    }

    @Test
    void productSupportsCrudAndValidatesReferences() throws Exception {
        String tipoNombre = "Tipo-" + UUID.randomUUID();
        String tipoCreated = mockMvc.perform(post("/api/v1/catalogos/tipos-producto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"%s\"}".formatted(tipoNombre)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        int tipoProductoId = objectMapper.readTree(tipoCreated).path("data").path("tipoProductoId").asInt();

        String catNombre = "Categoria-" + UUID.randomUUID();
        String catCreated = mockMvc.perform(post("/api/v1/catalogos/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"%s\"}".formatted(catNombre)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        int categoriaId = objectMapper.readTree(catCreated).path("data").path("id").asInt();

        String unidadNombre = "Unidad-" + UUID.randomUUID();
        String unidadCreated = mockMvc.perform(post("/api/v1/catalogos/unidades-medida")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"%s\"}".formatted(unidadNombre)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        int unidadMedidaId = objectMapper.readTree(unidadCreated).path("data").path("unidadMedidaId").asInt();

        String ruc = UUID.randomUUID().toString().replace("-", "").substring(0, 11);
        String provCreated = mockMvc.perform(post("/api/v1/catalogos/proveedores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ruc\":\"%s\",\"razonSocial\":\"Proveedor producto\"}".formatted(ruc)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        int proveedorId = objectMapper.readTree(provCreated).path("data").path("proveedorId").asInt();

        String codigo = "SKU-" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        String productPayload = """
                {"codigo":"%s","nombre":"Producto test","descripcion":"desc","tipoProductoId":%d,
                 "categoriaId":%d,"unidadMedidaId":%d,"proveedorId":%d,
                 "stockActual":10,"stockMinimo":2,"precioUnitario":5.5}
                """.formatted(codigo, tipoProductoId, categoriaId, unidadMedidaId, proveedorId);

        String productCreated = mockMvc.perform(post("/api/v1/catalogos/productos")
                        .contentType(MediaType.APPLICATION_JSON).content(productPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.codigo").value(codigo))
                .andExpect(jsonPath("$.data.activo").value(true))
                .andExpect(jsonPath("$.data.proveedorId").value(proveedorId))
                .andReturn().getResponse().getContentAsString();
        int productoId = objectMapper.readTree(productCreated).path("data").path("productoId").asInt();

        mockMvc.perform(post("/api/v1/catalogos/productos")
                        .contentType(MediaType.APPLICATION_JSON).content(productPayload))
                .andExpect(status().isConflict());

        mockMvc.perform(get("/api/v1/catalogos/productos").param("nombre", "Producto test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.datos[0].productoId").value(productoId));

        mockMvc.perform(get("/api/v1/catalogos/productos/{id}", productoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.codigo").value(codigo));

        mockMvc.perform(put("/api/v1/catalogos/productos/{id}", productoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productPayload.replace("\"nombre\":\"Producto test\"", "\"nombre\":\"Producto editado\"")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nombre").value("Producto editado"));

        mockMvc.perform(delete("/api/v1/catalogos/productos/{id}", productoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.activo").value(false));

        mockMvc.perform(put("/api/v1/catalogos/productos/{id}/estado", productoId)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"activo\":true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.activo").value(true));

        String invalidReferencePayload = """
                {"codigo":"%s","nombre":"Producto sin proveedor","tipoProductoId":%d,
                 "categoriaId":%d,"unidadMedidaId":%d,"proveedorId":999999,
                 "stockActual":1,"stockMinimo":0,"precioUnitario":1}
                """.formatted(codigo + "X", tipoProductoId, categoriaId, unidadMedidaId);
        mockMvc.perform(post("/api/v1/catalogos/productos")
                        .contentType(MediaType.APPLICATION_JSON).content(invalidReferencePayload))
                .andExpect(status().isNotFound());

        mockMvc.perform(post("/api/v1/catalogos/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Sin codigo\"}"))
                .andExpect(status().isBadRequest());
    }
}
