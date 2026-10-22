package com.agrihusac.SysInvent.service.almacen.impl;

import com.agrihusac.SysInvent.model.entity.KardexMovimientoEntity;
import com.agrihusac.SysInvent.model.entity.ProductoEntity;
import com.agrihusac.SysInvent.model.projection.ControlAlmacenListadoProjection;
import com.agrihusac.SysInvent.model.response.almacen.ControlAlmacenListadoResponse;
import com.agrihusac.SysInvent.model.response.almacen.ControlAlmacenMovimientoResponse;
import com.agrihusac.SysInvent.repository.CentroCostoRepository;
import com.agrihusac.SysInvent.repository.DestinoRepository;
import com.agrihusac.SysInvent.repository.KardexMovimientoRepository;
import com.agrihusac.SysInvent.repository.ParteEquipoRepository;
import com.agrihusac.SysInvent.repository.ProductoRepository;
import com.agrihusac.SysInvent.repository.UsuarioRepository;
import com.agrihusac.SysInvent.service.almacen.ControlAlmacenService;
import com.agrihusac.SysInvent.utils.CustomNotFoundException;
import com.agrihusac.SysInvent.utils.CustomPage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ControlAlmacenServiceImpl implements ControlAlmacenService {

    private final ProductoRepository productoRepository;
    private final KardexMovimientoRepository kardexMovimientoRepository;
    private final DestinoRepository destinoRepository;
    private final CentroCostoRepository centroCostoRepository;
    private final ParteEquipoRepository parteEquipoRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public CustomPage<ControlAlmacenMovimientoResponse> detalle(Integer productoId, Pageable pageable) {
        ProductoEntity producto = productoRepository.findById(productoId)
                .filter(item -> Boolean.TRUE.equals(item.getActivo()))
                .orElseThrow(() -> new CustomNotFoundException("El producto no existe o esta inactivo"));

        Page<ControlAlmacenMovimientoResponse> page = kardexMovimientoRepository
                .findByProductoIdOrderByFechaMovimientoAscKardexMovimientoIdAsc(productoId, pageable)
                .map(movimiento -> convertirMovimiento(movimiento, producto));
        return new CustomPage<>(page);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomPage<ControlAlmacenListadoResponse> listar(String busqueda, Integer tipoProductoId,
                                                             Integer categoriaId, String estado,
                                                             LocalDate fechaDesde, LocalDate fechaHasta,
                                                             Pageable pageable) {
        Page<ControlAlmacenListadoResponse> page = productoRepository.listarControlAlmacen(
                        normalizar(busqueda), tipoProductoId, categoriaId, normalizar(estado),
                        fechaDesde == null ? null : fechaDesde.atStartOfDay(),
                        fechaHasta == null ? null : fechaHasta.plusDays(1).atStartOfDay(), pageable)
                .map(this::convertir);
        return new CustomPage<>(page);
    }

    private ControlAlmacenListadoResponse convertir(ControlAlmacenListadoProjection projection) {
        return new ControlAlmacenListadoResponse(
                projection.getProductoId(), projection.getCodigo(), projection.getProducto(),
                projection.getUnidadMedida(), projection.getTipo(), projection.getCategoria(),
                projection.getProveedor(), projection.getEntradas(), projection.getSalidas(),
                projection.getStock(), projection.getEstado());
    }

    private ControlAlmacenMovimientoResponse convertirMovimiento(
            KardexMovimientoEntity movimiento, ProductoEntity producto) {
        return new ControlAlmacenMovimientoResponse(
                movimiento.getFechaMovimiento(),
                esEntrada(movimiento) ? "Entrada" : "Salida",
                movimiento.getNumeroDocumento(),
                movimiento.getCantidad(),
                producto.getUnidadMedida().getNombre(),
                nombreDestino(movimiento.getDestinoId()),
                nombreParteEquipo(movimiento.getParteEquipoId()),
                nombreCentroCosto(movimiento.getCentroCostoId()),
                nombreResponsable(movimiento.getUsuarioResponsable()),
                "REGISTRADO");
    }

    private boolean esEntrada(KardexMovimientoEntity movimiento) {
        return "ENTRADA_INGRESO".equals(movimiento.getTipoMovimiento())
                || "AJUSTE_ENTRADA".equals(movimiento.getTipoMovimiento());
    }

    private String nombreDestino(Integer id) {
        return id == null ? null : destinoRepository.findById(id).map(item -> item.getNombre()).orElse(null);
    }

    private String nombreParteEquipo(Integer id) {
        return id == null ? null : parteEquipoRepository.findById(id)
                .map(item -> item.getNombre()).orElse(null);
    }

    private String nombreCentroCosto(Integer id) {
        return id == null ? null : centroCostoRepository.findById(id)
                .map(item -> item.getNombre()).orElse(null);
    }

    private String nombreResponsable(String usuario) {
        if (usuario == null || usuario.isBlank()) {
            return usuario;
        }
        return usuarioRepository.findByUsuarioAndActivoTrue(usuario)
                .map(item -> java.util.stream.Stream.of(item.getNombres(), item.getApePaterno(), item.getApeMaterno())
                        .filter(Objects::nonNull)
                        .filter(nombre -> !nombre.isBlank())
                        .collect(Collectors.joining(" ")))
                .orElse(usuario);
    }

    private String normalizar(String valor) {
        return valor == null ? "" : valor.trim();
    }
}
