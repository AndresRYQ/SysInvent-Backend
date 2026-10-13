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
import com.agrihusac.SysInvent.repository.ContactoRepository;
import com.agrihusac.SysInvent.repository.IngresoAlmacenRepository;
import com.agrihusac.SysInvent.repository.ProductoRepository;
import com.agrihusac.SysInvent.repository.ProveedorRepository;
import com.agrihusac.SysInvent.repository.TipoDocumentoRepository;
import com.agrihusac.SysInvent.service.ingreso.IngresoAlmacenService;
import com.agrihusac.SysInvent.utils.CustomNotFoundException;
import com.agrihusac.SysInvent.utils.CustomPage;
import com.agrihusac.SysInvent.utils.MessageResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private static final String PREFIJO_NUMERO_INGRESO = "ING-";

    private final IngresoAlmacenRepository ingresoAlmacenRepository;
    private final ProveedorRepository proveedorRepository;
    private final ContactoRepository contactoRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final ProductoRepository productoRepository;

    @Override
    @Transactional
    public ResponseEntity<Object> registrar(IngresoAlmacenRequest request) {
        ProveedorEntity proveedor = proveedorRepository.findById(request.proveedorId())
                .filter(p -> Boolean.TRUE.equals(p.getActivo()))
                .orElseThrow(() -> new CustomNotFoundException(MSG_PROVEEDOR_NO_ENCONTRADO));

        ContactoEntity contacto = contactoRepository.findById(request.contactoId())
                .filter(c -> Boolean.TRUE.equals(c.getActivo()))
                .orElseThrow(() -> new CustomNotFoundException(MSG_CONTACTO_NO_ENCONTRADO));

        if (contacto.getProveedor() == null
                || !contacto.getProveedor().getProveedorId().equals(proveedor.getProveedorId())) {
            return MessageResponse.setResponse(false, HttpStatus.BAD_REQUEST, MSG_CONTACTO_NO_PERTENECE);
        }

        TipoDocumentoEntity tipoDocumento = tipoDocumentoRepository.findById(request.tipoDocumentoId())
                .filter(t -> Boolean.TRUE.equals(t.getActivo()))
                .orElseThrow(() -> new CustomNotFoundException(MSG_TIPO_DOCUMENTO_NO_ENCONTRADO));

        boolean documentoDuplicado = ingresoAlmacenRepository
                .existsByProveedor_ProveedorIdAndTipoDocumento_TipoDocumentoIdAndNumeroDocumentoIgnoreCase(
                        proveedor.getProveedorId(), tipoDocumento.getTipoDocumentoId(),
                        request.numeroDocumento().trim());
        if (documentoDuplicado) {
            return MessageResponse.setResponse(false, HttpStatus.CONFLICT, MSG_DOCUMENTO_DUPLICADO);
        }

        List<ProductoEntity> productos = new ArrayList<>();
        for (DetalleIngresoAlmacenRequest detalle : request.detalles()) {
            ProductoEntity producto = productoRepository.findById(detalle.productoId())
                    .filter(p -> Boolean.TRUE.equals(p.getActivo()))
                    .orElseThrow(() -> new CustomNotFoundException(MSG_PRODUCTO_NO_ENCONTRADO));
            productos.add(producto);
        }

        IngresoAlmacenEntity ingreso = new IngresoAlmacenEntity();
        ingreso.setNumeroIngreso(generarNumeroIngreso());
        ingreso.setFechaIngreso(request.fechaIngreso() == null ? LocalDate.now() : request.fechaIngreso());
        ingreso.setProveedor(proveedor);
        ingreso.setContacto(contacto);
        ingreso.setTipoDocumento(tipoDocumento);
        ingreso.setNumeroDocumento(request.numeroDocumento().trim());
        ingreso.setObservacion(request.observacion());
        ingreso.setEstado(IngresoAlmacenEntity.ESTADO_REGISTRADO);

        List<DetalleIngresoAlmacenEntity> detalles = new ArrayList<>();
        for (int i = 0; i < request.detalles().size(); i++) {
            DetalleIngresoAlmacenRequest detalleRequest = request.detalles().get(i);
            DetalleIngresoAlmacenEntity detalle = new DetalleIngresoAlmacenEntity();
            detalle.setIngreso(ingreso);
            detalle.setProducto(productos.get(i));
            detalle.setCantidad(detalleRequest.cantidad());
            detalle.setPrecioUnitario(detalleRequest.precioUnitario());
            detalles.add(detalle);
        }
        ingreso.getDetalles().addAll(detalles);

        IngresoAlmacenEntity guardado = ingresoAlmacenRepository.saveAndFlush(ingreso);

        for (DetalleIngresoAlmacenEntity detalle : new ArrayList<>(guardado.getDetalles())) {
            int actualizados = productoRepository.incrementarStock(
                    detalle.getProducto().getProductoId(), detalle.getCantidad());
            if (actualizados == 0) {
                throw new CustomNotFoundException(MSG_PRODUCTO_NO_ENCONTRADO);
            }
        }

        // El update atomico de stock limpia el contexto de persistencia; se
        // recarga el ingreso dentro de la misma transaccion para mapear la
        // respuesta con las asociaciones lazy accesibles.
        IngresoAlmacenEntity recargado = ingresoAlmacenRepository
                .findById(guardado.getIngresoAlmacenId())
                .orElseThrow(() -> new CustomNotFoundException(MSG_NO_ENCONTRADO));

        log.info("Ingreso de almacen {} registrado con {} detalle(s)",
                recargado.getNumeroIngreso(), recargado.getDetalles().size());

        return MessageResponse.setResponse(true, HttpStatus.CREATED, MSG_REGISTRADO,
                respuesta(recargado));
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtener(Integer id) {
        return ingresoAlmacenRepository.findById(id)
                .<ResponseEntity<Object>>map(ingreso -> MessageResponse.setResponse(
                        true, HttpStatus.OK, "Registro encontrado", respuesta(ingreso)))
                .orElseGet(() -> MessageResponse.setResponse(false, HttpStatus.NOT_FOUND, MSG_NO_ENCONTRADO));
    }

    @Override
    @Transactional(readOnly = true)
    public CustomPage<IngresoAlmacenResponse> listar(String numeroIngreso, Pageable pageable) {
        String filtro = numeroIngreso == null ? "" : numeroIngreso.trim();
        Page<IngresoAlmacenResponse> page = ingresoAlmacenRepository
                .findByNumeroIngresoContainingIgnoreCase(filtro, pageable)
                .map(this::respuesta);
        return new CustomPage<>(page);
    }

    private String generarNumeroIngreso() {
        long siguiente = ingresoAlmacenRepository.obtenerUltimoCorrelativo() + 1;
        return PREFIJO_NUMERO_INGRESO + String.format("%06d", siguiente);
    }

    private IngresoAlmacenResponse respuesta(IngresoAlmacenEntity ingreso) {
        List<DetalleIngresoAlmacenResponse> detalles = ingreso.getDetalles().stream()
                .map(this::respuestaDetalle)
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

    private DetalleIngresoAlmacenResponse respuestaDetalle(DetalleIngresoAlmacenEntity detalle) {
        ProductoEntity producto = detalle.getProducto();
        return new DetalleIngresoAlmacenResponse(
                detalle.getDetalleIngresoAlmacenId(),
                producto.getProductoId(),
                producto.getCodigo(),
                producto.getNombre(),
                producto.getUnidadMedida().getNombre(),
                detalle.getCantidad(),
                detalle.getPrecioUnitario(),
                detalle.getCantidad().multiply(detalle.getPrecioUnitario()));
    }
}
