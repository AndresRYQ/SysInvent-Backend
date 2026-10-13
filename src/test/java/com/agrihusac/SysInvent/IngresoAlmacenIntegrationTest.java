package com.agrihusac.SysInvent;

import com.agrihusac.SysInvent.model.entity.CategoriaEntity;
import com.agrihusac.SysInvent.model.entity.ContactoEntity;
import com.agrihusac.SysInvent.model.entity.ProductoEntity;
import com.agrihusac.SysInvent.model.entity.ProveedorEntity;
import com.agrihusac.SysInvent.model.entity.TipoDocumentoEntity;
import com.agrihusac.SysInvent.model.entity.TipoProductoEntity;
import com.agrihusac.SysInvent.model.entity.UnidadMedidaEntity;
import com.agrihusac.SysInvent.repository.CategoriaRepository;
import com.agrihusac.SysInvent.repository.ContactoRepository;
import com.agrihusac.SysInvent.repository.IngresoAlmacenRepository;
import com.agrihusac.SysInvent.repository.ProductoRepository;
import com.agrihusac.SysInvent.repository.ProveedorRepository;
import com.agrihusac.SysInvent.repository.TipoDocumentoRepository;
import com.agrihusac.SysInvent.repository.TipoProductoRepository;
import com.agrihusac.SysInvent.repository.UnidadMedidaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class IngresoAlmacenIntegrationTest {

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
    private IngresoAlmacenRepository ingresoAlmacenRepository;

    @Autowired
    private TipoProductoRepository tipoProductoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UnidadMedidaRepository unidadMedidaRepository;

    @Test
    void registraIngresoConUnProductoYActualizaStock() throws Exception {
        ProveedorEntity proveedor = crearProveedor();
        ContactoEntity contacto = crearContacto(proveedor);
        TipoDocumentoEntity tipoDocumento = crearTipoDocumento("Factura");
        ProductoEntity producto = crearProducto(proveedor, new BigDecimal("0"));

        ObjectNode body = cuerpoBase(proveedor, contacto, tipoDocumento);
        agregarDetalle(body, producto.getProductoId(), "5", "15.50");

        mockMvc.perform(post("/api/ingresos-almacen")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.estado").value("REGISTRADO"))
                .andExpect(jsonPath("$.data.detalles.length()").value(1))
                .andExpect(jsonPath("$.data.total").value(77.5));

        assertEquals(0, new BigDecimal("5").compareTo(
                productoRepository.findById(producto.getProductoId()).orElseThrow().getStockActual()));
    }

    @Test
    void registraIngresoConVariosProductos() throws Exception {
        ProveedorEntity proveedor = crearProveedor();
        ContactoEntity contacto = crearContacto(proveedor);
        TipoDocumentoEntity tipoDocumento = crearTipoDocumento("Boleta");
        ProductoEntity primero = crearProducto(proveedor, new BigDecimal("10"));
        ProductoEntity segundo = crearProducto(proveedor, new BigDecimal("0"));

        ObjectNode body = cuerpoBase(proveedor, contacto, tipoDocumento);
        agregarDetalle(body, primero.getProductoId(), "3", "20.00");
        agregarDetalle(body, segundo.getProductoId(), "7", "1.25");

        mockMvc.perform(post("/api/ingresos-almacen")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.detalles.length()").value(2));

        assertEquals(0, new BigDecimal("13").compareTo(
                productoRepository.findById(primero.getProductoId()).orElseThrow().getStockActual()));
        assertEquals(0, new BigDecimal("7").compareTo(
                productoRepository.findById(segundo.getProductoId()).orElseThrow().getStockActual()));
    }

    @Test
    void rechazaCantidadCero() throws Exception {
        ProveedorEntity proveedor = crearProveedor();
        ContactoEntity contacto = crearContacto(proveedor);
        TipoDocumentoEntity tipoDocumento = crearTipoDocumento("Guia");
        ProductoEntity producto = crearProducto(proveedor, new BigDecimal("4"));

        ObjectNode body = cuerpoBase(proveedor, contacto, tipoDocumento);
        agregarDetalle(body, producto.getProductoId(), "0", "10.00");

        mockMvc.perform(post("/api/ingresos-almacen")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());

        assertEquals(0, new BigDecimal("4").compareTo(
                productoRepository.findById(producto.getProductoId()).orElseThrow().getStockActual()));
    }

    @Test
    void rechazaCantidadNegativa() throws Exception {
        ProveedorEntity proveedor = crearProveedor();
        ContactoEntity contacto = crearContacto(proveedor);
        TipoDocumentoEntity tipoDocumento = crearTipoDocumento("Nota");
        ProductoEntity producto = crearProducto(proveedor, new BigDecimal("2"));

        ObjectNode body = cuerpoBase(proveedor, contacto, tipoDocumento);
        agregarDetalle(body, producto.getProductoId(), "-3", "10.00");

        mockMvc.perform(post("/api/ingresos-almacen")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());

        assertEquals(0, new BigDecimal("2").compareTo(
                productoRepository.findById(producto.getProductoId()).orElseThrow().getStockActual()));
    }

    @Test
    void rechazaProductoInexistente() throws Exception {
        ProveedorEntity proveedor = crearProveedor();
        ContactoEntity contacto = crearContacto(proveedor);
        TipoDocumentoEntity tipoDocumento = crearTipoDocumento("Factura");

        ObjectNode body = cuerpoBase(proveedor, contacto, tipoDocumento);
        agregarDetalle(body, 999999, "1", "10.00");

        mockMvc.perform(post("/api/ingresos-almacen")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isNotFound());
    }

    @Test
    void rechazaProveedorInexistente() throws Exception {
        ProveedorEntity proveedor = crearProveedor();
        ContactoEntity contacto = crearContacto(proveedor);
        TipoDocumentoEntity tipoDocumento = crearTipoDocumento("Factura");
        ProductoEntity producto = crearProducto(proveedor, new BigDecimal("0"));

        ObjectNode body = objectMapper.createObjectNode();
        body.put("proveedorId", 999999);
        body.put("contactoId", contacto.getContactoId());
        body.put("tipoDocumentoId", tipoDocumento.getTipoDocumentoId());
        body.put("numeroDocumento", "DOC-" + UUID.randomUUID());
        ArrayNode detalles = body.putArray("detalles");
        ObjectNode detalle = detalles.addObject();
        detalle.put("productoId", producto.getProductoId());
        detalle.put("cantidad", 1);
        detalle.put("precioUnitario", 10);

        mockMvc.perform(post("/api/ingresos-almacen")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isNotFound());
    }

    @Test
    void rechazaSolicitudSinDetalles() throws Exception {
        ProveedorEntity proveedor = crearProveedor();
        ContactoEntity contacto = crearContacto(proveedor);
        TipoDocumentoEntity tipoDocumento = crearTipoDocumento("Factura");

        ObjectNode body = cuerpoBase(proveedor, contacto, tipoDocumento);
        body.putArray("detalles");

        mockMvc.perform(post("/api/ingresos-almacen")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void revierteTodoCuandoFallaUnItemDelDetalle() throws Exception {
        ProveedorEntity proveedor = crearProveedor();
        ContactoEntity contacto = crearContacto(proveedor);
        TipoDocumentoEntity tipoDocumento = crearTipoDocumento("Factura");
        ProductoEntity valido = crearProducto(proveedor, new BigDecimal("6"));
        ProductoEntity inactivo = crearProducto(proveedor, new BigDecimal("1"));
        inactivo.setActivo(false);
        productoRepository.save(inactivo);

        String numeroDocumento = "DOC-" + UUID.randomUUID();
        ObjectNode body = cuerpoBase(proveedor, contacto, tipoDocumento);
        body.put("numeroDocumento", numeroDocumento);
        agregarDetalle(body, valido.getProductoId(), "4", "10.00");
        agregarDetalle(body, inactivo.getProductoId(), "2", "10.00");

        mockMvc.perform(post("/api/ingresos-almacen")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isNotFound());

        assertFalse(ingresoAlmacenRepository
                .existsByProveedor_ProveedorIdAndTipoDocumento_TipoDocumentoIdAndNumeroDocumentoIgnoreCase(
                        proveedor.getProveedorId(), tipoDocumento.getTipoDocumentoId(), numeroDocumento));
        assertEquals(0, new BigDecimal("6").compareTo(
                productoRepository.findById(valido.getProductoId()).orElseThrow().getStockActual()));
    }

    @Test
    void noDuplicaStockSiSeRepiteElMismoDocumentoOrigen() throws Exception {
        ProveedorEntity proveedor = crearProveedor();
        ContactoEntity contacto = crearContacto(proveedor);
        TipoDocumentoEntity tipoDocumento = crearTipoDocumento("Factura");
        ProductoEntity producto = crearProducto(proveedor, new BigDecimal("0"));

        ObjectNode body = cuerpoBase(proveedor, contacto, tipoDocumento);
        agregarDetalle(body, producto.getProductoId(), "8", "3.00");
        String json = objectMapper.writeValueAsString(body);

        mockMvc.perform(post("/api/ingresos-almacen")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/ingresos-almacen")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict());

        assertEquals(0, new BigDecimal("8").compareTo(
                productoRepository.findById(producto.getProductoId()).orElseThrow().getStockActual()));
    }

    @Test
    void rechazaContactoQueNoPerteneceAlProveedor() throws Exception {
        ProveedorEntity proveedor = crearProveedor();
        ProveedorEntity otroProveedor = crearProveedor();
        ContactoEntity contactoAjeno = crearContacto(otroProveedor);
        TipoDocumentoEntity tipoDocumento = crearTipoDocumento("Factura");
        ProductoEntity producto = crearProducto(proveedor, new BigDecimal("0"));

        ObjectNode body = cuerpoBase(proveedor, contactoAjeno, tipoDocumento);
        agregarDetalle(body, producto.getProductoId(), "1", "10.00");

        mockMvc.perform(post("/api/ingresos-almacen")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void rechazaTipoDocumentoInexistente() throws Exception {
        ProveedorEntity proveedor = crearProveedor();
        ContactoEntity contacto = crearContacto(proveedor);
        ProductoEntity producto = crearProducto(proveedor, new BigDecimal("0"));

        ObjectNode body = cuerpoBase(proveedor, contacto, null);
        body.put("tipoDocumentoId", 999999);
        agregarDetalle(body, producto.getProductoId(), "1", "10.00");

        mockMvc.perform(post("/api/ingresos-almacen")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isNotFound());
    }

    @Test
    void consultaIngresoPorIdTrasElRegistro() throws Exception {
        ProveedorEntity proveedor = crearProveedor();
        ContactoEntity contacto = crearContacto(proveedor);
        TipoDocumentoEntity tipoDocumento = crearTipoDocumento("Factura");
        ProductoEntity producto = crearProducto(proveedor, new BigDecimal("0"));

        ObjectNode body = cuerpoBase(proveedor, contacto, tipoDocumento);
        agregarDetalle(body, producto.getProductoId(), "2", "9.99");

        MvcResult resultado = mockMvc.perform(post("/api/ingresos-almacen")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andReturn();

        int id = objectMapper.readTree(resultado.getResponse().getContentAsString())
                .path("data").path("ingresoAlmacenId").asInt();

        mockMvc.perform(get("/api/ingresos-almacen/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.ingresoAlmacenId").value(id))
                .andExpect(jsonPath("$.data.numeroIngreso").isNotEmpty())
                .andExpect(jsonPath("$.data.detalles[0].productoId").value(producto.getProductoId()));
    }

    private ObjectNode cuerpoBase(ProveedorEntity proveedor, ContactoEntity contacto,
            TipoDocumentoEntity tipoDocumento) {
        ObjectNode body = objectMapper.createObjectNode();
        body.put("proveedorId", proveedor.getProveedorId());
        body.put("contactoId", contacto.getContactoId());
        if (tipoDocumento != null) {
            body.put("tipoDocumentoId", tipoDocumento.getTipoDocumentoId());
        }
        body.put("numeroDocumento", "DOC-" + UUID.randomUUID());
        body.put("observacion", "Ingreso de prueba");
        return body;
    }

    private void agregarDetalle(ObjectNode body, Integer productoId, String cantidad, String precio) {
        ArrayNode detalles = body.withArray("detalles");
        ObjectNode detalle = detalles.addObject();
        detalle.put("productoId", productoId);
        detalle.put("cantidad", cantidad);
        detalle.put("precioUnitario", precio);
    }

    private ProveedorEntity crearProveedor() {
        String sufijo = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        ProveedorEntity proveedor = new ProveedorEntity();
        proveedor.setRuc("20" + sufijo);
        proveedor.setRazonSocial("Proveedor " + sufijo);
        proveedor.setActivo(true);
        return proveedorRepository.save(proveedor);
    }

    private ContactoEntity crearContacto(ProveedorEntity proveedor) {
        ContactoEntity contacto = new ContactoEntity();
        contacto.setProveedor(proveedor);
        contacto.setNombreCompleto("Contacto " + UUID.randomUUID().toString().substring(0, 8));
        contacto.setActivo(true);
        return contactoRepository.save(contacto);
    }

    private TipoDocumentoEntity crearTipoDocumento(String nombreBase) {
        TipoDocumentoEntity tipoDocumento = new TipoDocumentoEntity();
        tipoDocumento.setNombre(nombreBase + "-" + UUID.randomUUID().toString().substring(0, 8));
        tipoDocumento.setActivo(true);
        return tipoDocumentoRepository.save(tipoDocumento);
    }

    private ProductoEntity crearProducto(ProveedorEntity proveedor, BigDecimal stockInicial) {
        String sufijo = UUID.randomUUID().toString().replace("-", "").substring(0, 10);

        TipoProductoEntity tipoProducto = new TipoProductoEntity();
        tipoProducto.setNombre("Tipo-" + sufijo);
        tipoProducto.setActivo(true);
        tipoProductoRepository.save(tipoProducto);

        CategoriaEntity categoria = new CategoriaEntity();
        categoria.setNombre("Cat-" + sufijo);
        categoria.setActivo(true);
        categoriaRepository.save(categoria);

        UnidadMedidaEntity unidadMedida = new UnidadMedidaEntity();
        unidadMedida.setNombre("UM-" + sufijo);
        unidadMedida.setActivo(true);
        unidadMedidaRepository.save(unidadMedida);

        ProductoEntity producto = new ProductoEntity();
        producto.setCodigo("P" + sufijo);
        producto.setNombre("Producto " + sufijo);
        producto.setTipoProducto(tipoProducto);
        producto.setCategoria(categoria);
        producto.setUnidadMedida(unidadMedida);
        producto.setProveedor(proveedor);
        producto.setStockActual(stockInicial);
        producto.setStockMinimo(BigDecimal.ZERO);
        producto.setPrecioUnitario(new BigDecimal("10.00"));
        producto.setActivo(true);
        return productoRepository.save(producto);
    }
}
