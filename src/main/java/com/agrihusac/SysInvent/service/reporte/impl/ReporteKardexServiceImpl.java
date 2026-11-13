package com.agrihusac.SysInvent.service.reporte.impl;

import com.agrihusac.SysInvent.model.entity.KardexMovimientoEntity;
import com.agrihusac.SysInvent.model.entity.ProductoEntity;
import com.agrihusac.SysInvent.model.entity.UsuarioEntity;
import com.agrihusac.SysInvent.model.request.reporte.ReporteKardexFiltro;
import com.agrihusac.SysInvent.model.response.reporte.ReporteKardexResponse;
import com.agrihusac.SysInvent.repository.KardexMovimientoRepository;
import com.agrihusac.SysInvent.repository.ProductoRepository;
import com.agrihusac.SysInvent.repository.UsuarioRepository;
import com.agrihusac.SysInvent.service.reporte.ReporteKardexService;
import com.agrihusac.SysInvent.utils.TipoMovimientoKardex;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ReporteKardexServiceImpl implements ReporteKardexService {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");

    private final KardexMovimientoRepository kardexRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public ReporteKardexResponse generar(ReporteKardexFiltro filtro) {
        if (filtro.desde() != null && filtro.hasta() != null && filtro.desde().isAfter(filtro.hasta())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La fecha inicial debe ser anterior o igual a la fecha final.");
        }

        List<KardexMovimientoEntity> movimientos = kardexRepository.buscarParaReporte(
                filtro.productoId(),
                resolverTipos(filtro.tipoMovimiento()),
                filtro.desde() == null ? null : filtro.desde().atStartOfDay(),
                filtro.hasta() == null ? null : filtro.hasta().plusDays(1).atStartOfDay());

        Map<Integer, ProductoEntity> productos = productoRepository.findAllConUnidadMedida().stream()
                .collect(Collectors.toMap(ProductoEntity::getProductoId, Function.identity()));
        Map<String, String> responsables = nombresResponsables(movimientos);

        List<ReporteKardexResponse.Movimiento> detalle = movimientos.stream()
                .map(movimiento -> convertir(movimiento, productos.get(movimiento.getProductoId()), responsables))
                .toList();

        List<ReporteKardexResponse.Stock> stock = productos.values().stream()
                .filter(producto -> Boolean.TRUE.equals(producto.getActivo()))
                .filter(producto -> filtro.productoId() == null
                        || filtro.productoId().equals(producto.getProductoId()))
                .sorted((a, b) -> a.getNombre().compareToIgnoreCase(b.getNombre()))
                .map(this::convertirStock)
                .toList();

        return new ReporteKardexResponse(resumir(detalle), detalle, stock);
    }

    /**
     * Convierte el filtro de tipo en la lista de tipos del Kardex a consultar.
     * Acepta los grupos ENTRADA y SALIDA o un tipo exacto.
     */
    public static List<String> resolverTipos(String tipoMovimiento) {
        if (tipoMovimiento == null || tipoMovimiento.isBlank()) {
            return TipoMovimientoKardex.TODOS;
        }
        String tipo = tipoMovimiento.trim().toUpperCase(Locale.ROOT);
        if ("ENTRADA".equals(tipo)) {
            return TipoMovimientoKardex.ENTRADAS;
        }
        if ("SALIDA".equals(tipo)) {
            return TipoMovimientoKardex.SALIDAS;
        }
        if (TipoMovimientoKardex.TODOS.contains(tipo)) {
            return List.of(tipo);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "El tipo de movimiento no es válido. Use ENTRADA, SALIDA o un tipo de Kardex existente.");
    }

    private Map<String, String> nombresResponsables(List<KardexMovimientoEntity> movimientos) {
        Set<String> usuarios = movimientos.stream()
                .map(KardexMovimientoEntity::getUsuarioResponsable)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (usuarios.isEmpty()) {
            return Map.of();
        }
        return usuarioRepository.findByUsuarioIn(usuarios).stream()
                .collect(Collectors.toMap(UsuarioEntity::getUsuario, this::nombreCompleto, (a, b) -> a));
    }

    private String nombreCompleto(UsuarioEntity usuario) {
        String nombre = Stream.of(usuario.getNombres(), usuario.getApePaterno(), usuario.getApeMaterno())
                .filter(parte -> parte != null && !parte.isBlank())
                .map(String::trim)
                .collect(Collectors.joining(" "));
        return nombre.isBlank() ? usuario.getUsuario() : nombre;
    }

    private ReporteKardexResponse.Movimiento convertir(KardexMovimientoEntity movimiento, ProductoEntity producto,
                                                       Map<String, String> responsables) {
        boolean entrada = TipoMovimientoKardex.esEntrada(movimiento.getTipoMovimiento());
        return new ReporteKardexResponse.Movimiento(
                movimiento.getKardexMovimientoId(),
                movimiento.getProductoId(),
                producto == null ? null : producto.getCodigo(),
                producto == null ? "Producto no encontrado" : producto.getNombre(),
                producto == null || producto.getUnidadMedida() == null ? null : producto.getUnidadMedida().getNombre(),
                movimiento.getFechaMovimiento(),
                movimiento.getFechaMovimiento().format(FORMATO_FECHA),
                movimiento.getFechaMovimiento().format(FORMATO_HORA),
                movimiento.getTipoMovimiento(),
                TipoMovimientoKardex.describir(movimiento.getTipoMovimiento()),
                entrada ? "ENTRADA" : "SALIDA",
                entrada ? movimiento.getCantidad() : BigDecimal.ZERO,
                entrada ? BigDecimal.ZERO : movimiento.getCantidad(),
                movimiento.getSaldoAnterior(),
                movimiento.getSaldoNuevo(),
                movimiento.getPrecioUnitario(),
                movimiento.getNumeroDocumento(),
                responsables.getOrDefault(movimiento.getUsuarioResponsable(), movimiento.getUsuarioResponsable()));
    }

    private ReporteKardexResponse.Stock convertirStock(ProductoEntity producto) {
        return new ReporteKardexResponse.Stock(
                producto.getProductoId(), producto.getCodigo(), producto.getNombre(),
                producto.getUnidadMedida() == null ? null : producto.getUnidadMedida().getNombre(),
                producto.getStockActual(), producto.getStockMinimo(),
                producto.getStockActual().compareTo(producto.getStockMinimo()) <= 0);
    }

    private ReporteKardexResponse.Resumen resumir(List<ReporteKardexResponse.Movimiento> movimientos) {
        long entradas = movimientos.stream().filter(m -> "ENTRADA".equals(m.naturaleza())).count();
        BigDecimal cantidadEntrada = movimientos.stream()
                .map(ReporteKardexResponse.Movimiento::entrada).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal cantidadSalida = movimientos.stream()
                .map(ReporteKardexResponse.Movimiento::salida).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new ReporteKardexResponse.Resumen(
                movimientos.size(), entradas, movimientos.size() - entradas, cantidadEntrada, cantidadSalida);
    }
}
