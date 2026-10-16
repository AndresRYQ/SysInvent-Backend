package com.agrihusac.SysInvent.service.ingreso.impl;

import com.agrihusac.SysInvent.model.entity.ContactoEntity;
import com.agrihusac.SysInvent.model.entity.DetalleIngresoAlmacenEntity;
import com.agrihusac.SysInvent.model.entity.IngresoAlmacenEntity;
import com.agrihusac.SysInvent.model.entity.KardexMovimientoEntity;
import com.agrihusac.SysInvent.model.entity.ProductoEntity;
import com.agrihusac.SysInvent.model.entity.ProveedorEntity;
import com.agrihusac.SysInvent.model.entity.TipoDocumentoEntity;
import com.agrihusac.SysInvent.model.request.ingreso.DetalleIngresoAlmacenRequest;
import com.agrihusac.SysInvent.model.request.ingreso.IngresoAlmacenRequest;
import com.agrihusac.SysInvent.model.response.ingreso.IngresoAlmacenResponse;
import com.agrihusac.SysInvent.repository.ContactoRepository;
import com.agrihusac.SysInvent.repository.IngresoAlmacenRepository;
import com.agrihusac.SysInvent.repository.KardexMovimientoRepository;
import com.agrihusac.SysInvent.repository.ProductoRepository;
import com.agrihusac.SysInvent.repository.ProveedorRepository;
import com.agrihusac.SysInvent.repository.TipoDocumentoRepository;
import com.agrihusac.SysInvent.service.ingreso.IngresoAlmacenService;
import com.agrihusac.SysInvent.utils.CustomNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IngresoAlmacenServiceImpl implements IngresoAlmacenService {

    private static final String REGISTRADO = "REGISTRADO";
    private static final String ENTRADA_INGRESO = "ENTRADA_INGRESO";
    private static final BigDecimal ZERO = BigDecimal.ZERO.setScale(4);

    private final IngresoAlmacenRepository ingresoRepository;
    private final KardexMovimientoRepository kardexRepository;
    private final ProductoRepository productoRepository;
    private final ProveedorRepository proveedorRepository;
    private final ContactoRepository contactoRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<IngresoAlmacenResponse> listar() {
        return ingresoRepository.findAll().stream()
                .sorted(Comparator.comparing(IngresoAlmacenEntity::getIngresoAlmacenId).reversed())
                .map(this::convertir)
                .toList();
    }

    @Override
    @Transactional
    public IngresoAlmacenResponse crear(IngresoAlmacenRequest request) {
        obtenerProveedor(request.proveedorId());
        ContactoEntity contacto = contactoRepository.findById(request.contactoId())
                .filter(ContactoEntity::getActivo)
                .orElseThrow(() -> new CustomNotFoundException("No se encontró el contacto activo indicado."));
        if (!contacto.getProveedor().getProveedorId().equals(request.proveedorId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El contacto no pertenece al proveedor indicado.");
        }
        tipoDocumentoRepository.findById(request.tipoDocumentoId())
                .filter(TipoDocumentoEntity::getActivo)
                .orElseThrow(() -> new CustomNotFoundException("No se encontró el tipo de documento activo indicado."));

        Map<Integer, ProductoEntity> productos = new HashMap<>();
        request.detalles().stream().map(DetalleIngresoAlmacenRequest::productoId).distinct().sorted()
                .forEach(id -> productos.put(id, productoRepository.buscarParaActualizar(id)
                        .filter(producto -> Boolean.TRUE.equals(producto.getActivo()))
                        .orElseThrow(() -> new CustomNotFoundException("No se encontró el producto activo " + id + "."))));

        IngresoAlmacenEntity ingreso = new IngresoAlmacenEntity();
        ingreso.setNumeroIngreso("P-" + UUID.randomUUID().toString().replace("-", "").substring(0, 20));
        ingreso.setFechaIngreso(request.fechaIngreso());
        ingreso.setProveedorId(request.proveedorId());
        ingreso.setContactoId(request.contactoId());
        ingreso.setTipoDocumentoId(request.tipoDocumentoId());
        ingreso.setNumeroDocumento(request.numeroDocumento().trim());
        ingreso.setObservacion(request.observacion() == null ? null : request.observacion().trim());
        ingreso.setEstado(REGISTRADO);

        for (DetalleIngresoAlmacenRequest linea : request.detalles()) {
            DetalleIngresoAlmacenEntity detalle = new DetalleIngresoAlmacenEntity();
            detalle.setProductoId(linea.productoId());
            detalle.setCantidad(normalizar(linea.cantidad()));
            detalle.setPrecioUnitario(normalizar(linea.precioUnitario()));
            ingreso.agregarDetalle(detalle);
        }
        ingreso = ingresoRepository.saveAndFlush(ingreso);
        ingreso.setNumeroIngreso(String.format(Locale.ROOT, "ING-%d-%04d",
                request.fechaIngreso().getYear(), ingreso.getIngresoAlmacenId()));
        ingreso = ingresoRepository.saveAndFlush(ingreso);

        List<KardexMovimientoEntity> movimientos = new ArrayList<>();
        for (DetalleIngresoAlmacenRequest linea : request.detalles()) {
            ProductoEntity producto = productos.get(linea.productoId());
            BigDecimal saldoAnterior = normalizar(producto.getStockActual());
            BigDecimal cantidad = normalizar(linea.cantidad());
            BigDecimal saldoNuevo = saldoAnterior.add(cantidad);
            producto.setStockActual(saldoNuevo);
            producto.setPrecioUnitario(normalizar(linea.precioUnitario()));
            productoRepository.save(producto);

            KardexMovimientoEntity movimiento = new KardexMovimientoEntity();
            movimiento.setIngresoAlmacenId(ingreso.getIngresoAlmacenId());
            movimiento.setProductoId(producto.getProductoId());
            movimiento.setUsuarioResponsable(usuarioActual());
            movimiento.setTipoMovimiento(ENTRADA_INGRESO);
            movimiento.setFechaMovimiento(LocalDateTime.now());
            movimiento.setCantidad(cantidad);
            movimiento.setSaldoAnterior(saldoAnterior);
            movimiento.setSaldoNuevo(saldoNuevo);
            movimiento.setPrecioUnitario(normalizar(linea.precioUnitario()));
            movimiento.setNumeroDocumento(ingreso.getNumeroIngreso());
            movimientos.add(movimiento);
        }
        kardexRepository.saveAll(movimientos);
        return convertir(ingreso);
    }

    private ProveedorEntity obtenerProveedor(Integer id) {
        return proveedorRepository.findById(id).filter(ProveedorEntity::getActivo)
                .orElseThrow(() -> new CustomNotFoundException("No se encontró el proveedor activo indicado."));
    }

    private IngresoAlmacenResponse convertir(IngresoAlmacenEntity ingreso) {
        List<IngresoAlmacenResponse.Detalle> detalles = ingreso.getDetalles().stream().map(detalle -> {
            ProductoEntity producto = productoRepository.findById(detalle.getProductoId())
                    .orElseThrow(() -> new CustomNotFoundException("No se encontró el producto del ingreso."));
            return new IngresoAlmacenResponse.Detalle(
                    producto.getProductoId(), producto.getCodigo(), producto.getNombre(),
                    detalle.getCantidad(), detalle.getPrecioUnitario(), producto.getStockActual());
        }).toList();
        return new IngresoAlmacenResponse(
                ingreso.getIngresoAlmacenId(), ingreso.getNumeroIngreso(), ingreso.getFechaIngreso(),
                ingreso.getProveedorId(), ingreso.getContactoId(), ingreso.getTipoDocumentoId(),
                ingreso.getNumeroDocumento(), ingreso.getEstado(), ingreso.getFechaRegistro(), detalles);
    }

    private String usuarioActual() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication == null || authentication.getName() == null
                ? "sistema"
                : authentication.getName();
    }

    private BigDecimal normalizar(BigDecimal valor) {
        return valor == null ? ZERO : valor.setScale(4, java.math.RoundingMode.HALF_UP);
    }
}
