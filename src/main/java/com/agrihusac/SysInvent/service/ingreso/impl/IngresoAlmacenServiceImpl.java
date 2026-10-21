package com.agrihusac.SysInvent.service.ingreso.impl;

import com.agrihusac.SysInvent.model.entity.ContactoEntity;
import com.agrihusac.SysInvent.model.entity.DetalleIngresoAlmacenEntity;
import com.agrihusac.SysInvent.model.entity.IngresoAlmacenEntity;
import com.agrihusac.SysInvent.model.entity.ProductoEntity;
import com.agrihusac.SysInvent.model.entity.ProveedorEntity;
import com.agrihusac.SysInvent.model.entity.TipoDocumentoEntity;
import com.agrihusac.SysInvent.model.request.ingreso.DetalleIngresoAlmacenRequest;
import com.agrihusac.SysInvent.model.request.ingreso.IngresoAlmacenRequest;
import com.agrihusac.SysInvent.model.response.ingreso.DetalleIngresoAlmacenResponse;
import com.agrihusac.SysInvent.model.response.ingreso.IngresoAlmacenResponse;
import com.agrihusac.SysInvent.model.response.ingreso.IngresoAlmacenListadoResponse;
import com.agrihusac.SysInvent.model.projection.IngresoAlmacenListadoProjection;
import com.agrihusac.SysInvent.repository.ContactoRepository;
import com.agrihusac.SysInvent.repository.IngresoAlmacenRepository;
import com.agrihusac.SysInvent.repository.ProductoRepository;
import com.agrihusac.SysInvent.repository.ProveedorRepository;
import com.agrihusac.SysInvent.repository.TipoDocumentoRepository;
import com.agrihusac.SysInvent.service.ingreso.IngresoAlmacenService;
import com.agrihusac.SysInvent.utils.CustomNotFoundException;
import com.agrihusac.SysInvent.utils.CustomPage;
import com.agrihusac.SysInvent.utils.MessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class IngresoAlmacenServiceImpl implements IngresoAlmacenService {

    private static final String MSG_REGISTRADO = "Ingreso de almacen registrado correctamente";
    private static final String MSG_NO_ENCONTRADO = "No se encontro el ingreso de almacen";
    private static final String MSG_PROVEEDOR_NO_ENCONTRADO = "El proveedor no existe o esta inactivo";
    private static final String MSG_CONTACTO_NO_ENCONTRADO = "El contacto no existe o esta inactivo";
    private static final String MSG_CONTACTO_NO_PERTENECE = "El contacto no pertenece al proveedor indicado";
    private static final String MSG_TIPO_DOCUMENTO_NO_ENCONTRADO = "El tipo de documento no existe o esta inactivo";
    private static final String MSG_DOCUMENTO_DUPLICADO = "Ya existe un ingreso registrado con ese documento de origen";
    private static final String MSG_PRODUCTO_NO_ENCONTRADO = "Uno o mas productos no existen o estan inactivos";
    private static final BigDecimal ZERO = BigDecimal.ZERO.setScale(4);

    private final IngresoAlmacenRepository ingresoAlmacenRepository;
    private final ProveedorRepository proveedorRepository;
    private final ContactoRepository contactoRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final ProductoRepository productoRepository;

    @Override
    @Transactional
    public ResponseEntity<Object> registrar(IngresoAlmacenRequest request) {
        ProveedorEntity proveedor = proveedorRepository.findById(request.proveedorId())
                .filter(entity -> Boolean.TRUE.equals(entity.getActivo()))
                .orElseThrow(() -> new CustomNotFoundException(MSG_PROVEEDOR_NO_ENCONTRADO));

        ContactoEntity contacto = contactoRepository.findById(request.contactoId())
                .filter(entity -> Boolean.TRUE.equals(entity.getActivo()))
                .orElseThrow(() -> new CustomNotFoundException(MSG_CONTACTO_NO_ENCONTRADO));
        if (contacto.getProveedor() == null
                || !contacto.getProveedor().getProveedorId().equals(proveedor.getProveedorId())) {
            return MessageResponse.setResponse(false, HttpStatus.BAD_REQUEST, MSG_CONTACTO_NO_PERTENECE);
        }

        TipoDocumentoEntity tipoDocumento = tipoDocumentoRepository.findById(request.tipoDocumentoId())
                .filter(entity -> Boolean.TRUE.equals(entity.getActivo()))
                .orElseThrow(() -> new CustomNotFoundException(MSG_TIPO_DOCUMENTO_NO_ENCONTRADO));

        String numeroDocumento = request.numeroDocumento().trim();
        if (ingresoAlmacenRepository
                .existsByProveedor_ProveedorIdAndTipoDocumento_TipoDocumentoIdAndNumeroDocumentoIgnoreCase(
                        proveedor.getProveedorId(), tipoDocumento.getTipoDocumentoId(), numeroDocumento)) {
            return MessageResponse.setResponse(false, HttpStatus.CONFLICT, MSG_DOCUMENTO_DUPLICADO);
        }

        Map<Integer, ProductoEntity> productos = new HashMap<>();
        request.detalles().stream()
                .map(DetalleIngresoAlmacenRequest::productoId)
                .distinct()
                .sorted()
                .forEach(id -> productos.put(id, productoRepository.buscarParaActualizar(id)
                        .filter(producto -> Boolean.TRUE.equals(producto.getActivo()))
                        .orElseThrow(() -> new CustomNotFoundException(MSG_PRODUCTO_NO_ENCONTRADO))));

        IngresoAlmacenEntity ingreso = new IngresoAlmacenEntity();
        ingreso.setNumeroIngreso("TMP-" + UUID.randomUUID().toString().replace("-", "").substring(0, 20));
        ingreso.setFechaIngreso(request.fechaIngreso() == null ? LocalDate.now() : request.fechaIngreso());
        ingreso.setProveedor(proveedor);
        ingreso.setContacto(contacto);
        ingreso.setTipoDocumento(tipoDocumento);
        ingreso.setNumeroDocumento(numeroDocumento);
        ingreso.setObservacion(request.observacion() == null ? null : request.observacion().trim());
        ingreso.setEstado(IngresoAlmacenEntity.ESTADO_REGISTRADO);

        for (DetalleIngresoAlmacenRequest linea : request.detalles()) {
            DetalleIngresoAlmacenEntity detalle = new DetalleIngresoAlmacenEntity();
            detalle.setProducto(productos.get(linea.productoId()));
            detalle.setCantidad(normalizar(linea.cantidad()));
            detalle.setPrecioUnitario(normalizar(linea.precioUnitario()));
            ingreso.agregarDetalle(detalle);
        }

        ingreso = ingresoAlmacenRepository.saveAndFlush(ingreso);
        ingreso.setNumeroIngreso(String.format("ING-%06d", ingreso.getIngresoAlmacenId()));
        ingreso = ingresoAlmacenRepository.saveAndFlush(ingreso);

        for (DetalleIngresoAlmacenEntity detalle : ingreso.getDetalles()) {
            ProductoEntity producto = productos.get(detalle.getProducto().getProductoId());
            BigDecimal saldoAnterior = normalizar(producto.getStockActual());
            BigDecimal cantidad = normalizar(detalle.getCantidad());
            BigDecimal saldoNuevo = saldoAnterior.add(cantidad);

            producto.setStockActual(saldoNuevo);
            producto.setPrecioUnitario(normalizar(detalle.getPrecioUnitario()));
            productoRepository.save(producto);
        }

        log.info("Ingreso de almacen {} registrado con {} detalle(s); Kardex omitido temporalmente",
                ingreso.getNumeroIngreso(), ingreso.getDetalles().size());

        return MessageResponse.setResponse(true, HttpStatus.CREATED, MSG_REGISTRADO);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtener(Integer id) {
        return ingresoAlmacenRepository.findById(id)
                .<ResponseEntity<Object>>map(ingreso -> MessageResponse.setResponse(
                        true, HttpStatus.OK, "Registro encontrado", respuesta(ingreso, Map.of())))
                .orElseGet(() -> MessageResponse.setResponse(false, HttpStatus.NOT_FOUND, MSG_NO_ENCONTRADO));
    }

    @Override
    @Transactional(readOnly = true)
    public CustomPage<IngresoAlmacenListadoResponse> listar(String busqueda, Integer proveedorId,
                                                            String estado, LocalDate fechaDesde,
                                                            LocalDate fechaHasta, Pageable pageable) {
        Page<IngresoAlmacenListadoResponse> page = ingresoAlmacenRepository
                .listarResumen(normalizarFiltro(busqueda), proveedorId, normalizarFiltro(estado),
                        fechaDesde, fechaHasta, pageable)
                .map(this::respuestaListado);
        return new CustomPage<>(page);
    }

    private IngresoAlmacenListadoResponse respuestaListado(IngresoAlmacenListadoProjection projection) {
        return new IngresoAlmacenListadoResponse(
                projection.getIngresoAlmacenId(), projection.getNumeroIngreso(),
                projection.getFechaIngreso(), projection.getProveedor(), projection.getContacto(),
                projection.getDocumento(), projection.getProductos(), projection.getCantidadIngresada(),
                projection.getTotal(), projection.getEstado());
    }

    private String normalizarFiltro(String valor) {
        return valor == null ? "" : valor.trim();
    }

    private IngresoAlmacenResponse respuesta(
            IngresoAlmacenEntity ingreso, Map<Integer, BigDecimal> stockPorDetalle) {
        List<DetalleIngresoAlmacenResponse> detalles = ingreso.getDetalles().stream()
                .sorted(Comparator.comparing(DetalleIngresoAlmacenEntity::getDetalleIngresoAlmacenId))
                .map(detalle -> respuestaDetalle(detalle, stockPorDetalle))
                .toList();
        BigDecimal total = detalles.stream()
                .map(DetalleIngresoAlmacenResponse::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new IngresoAlmacenResponse(
                ingreso.getIngresoAlmacenId(),
                ingreso.getNumeroIngreso(),
                ingreso.getFechaIngreso(),
                ingreso.getFechaRegistro(),
                ingreso.getProveedor().getProveedorId(),
                ingreso.getProveedor().getRazonSocial(),
                ingreso.getContacto().getContactoId(),
                ingreso.getContacto().getNombreCompleto(),
                ingreso.getTipoDocumento().getTipoDocumentoId(),
                ingreso.getTipoDocumento().getNombre(),
                ingreso.getNumeroDocumento(),
                ingreso.getObservacion(),
                ingreso.getEstado(),
                total,
                detalles);
    }

    private DetalleIngresoAlmacenResponse respuestaDetalle(
            DetalleIngresoAlmacenEntity detalle, Map<Integer, BigDecimal> stockPorDetalle) {
        ProductoEntity producto = detalle.getProducto();
        return new DetalleIngresoAlmacenResponse(
                detalle.getDetalleIngresoAlmacenId(),
                producto.getProductoId(),
                producto.getCodigo(),
                producto.getNombre(),
                producto.getUnidadMedida().getNombre(),
                detalle.getCantidad(),
                detalle.getPrecioUnitario(),
                detalle.getCantidad().multiply(detalle.getPrecioUnitario()),
                stockPorDetalle.getOrDefault(
                        detalle.getDetalleIngresoAlmacenId(), normalizar(producto.getStockActual())));
    }

    private BigDecimal normalizar(BigDecimal valor) {
        return valor == null ? ZERO : valor.setScale(4, RoundingMode.HALF_UP);
    }
}
