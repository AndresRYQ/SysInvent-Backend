package com.agrihusac.SysInvent.service.valeconsumo.impl;

import com.agrihusac.SysInvent.common.StockInsuficienteException;
import com.agrihusac.SysInvent.common.ValeConsumoConflictException;
import com.agrihusac.SysInvent.common.ValeConsumoValidationException;
import com.agrihusac.SysInvent.model.entity.CentroCostoEntity;
import com.agrihusac.SysInvent.model.entity.DetalleValeConsumoEntity;
import com.agrihusac.SysInvent.model.entity.DestinoEntity;
import com.agrihusac.SysInvent.model.entity.DistribucionValeConsumoEntity;
import com.agrihusac.SysInvent.model.entity.KardexMovimientoEntity;
import com.agrihusac.SysInvent.model.entity.ParteEquipoEntity;
import com.agrihusac.SysInvent.model.entity.ProductoEntity;
import com.agrihusac.SysInvent.model.entity.ValeConsumoEntity;
import com.agrihusac.SysInvent.model.request.valeconsumo.DetalleValeConsumoRequest;
import com.agrihusac.SysInvent.model.request.valeconsumo.DistribucionValeConsumoRequest;
import com.agrihusac.SysInvent.model.request.valeconsumo.ValeConsumoRequest;
import com.agrihusac.SysInvent.model.response.valeconsumo.DetalleValeConsumoResponse;
import com.agrihusac.SysInvent.model.response.valeconsumo.DistribucionValeConsumoResponse;
import com.agrihusac.SysInvent.model.response.valeconsumo.KardexMovimientoResponse;
import com.agrihusac.SysInvent.model.response.valeconsumo.ValeConsumoResponse;
import com.agrihusac.SysInvent.model.response.valeconsumo.ValeConsumoListadoResponse;
import com.agrihusac.SysInvent.model.projection.ValeConsumoListadoProjection;
import com.agrihusac.SysInvent.repository.CentroCostoRepository;
import com.agrihusac.SysInvent.repository.DestinoRepository;
import com.agrihusac.SysInvent.repository.KardexMovimientoRepository;
import com.agrihusac.SysInvent.repository.ParteEquipoRepository;
import com.agrihusac.SysInvent.repository.ProductoRepository;
import com.agrihusac.SysInvent.repository.UnidadMedidaRepository;
import com.agrihusac.SysInvent.repository.ValeConsumoRepository;
import com.agrihusac.SysInvent.utils.CustomNotFoundException;
import com.agrihusac.SysInvent.utils.CustomPage;
import com.agrihusac.SysInvent.service.valeconsumo.ValeConsumoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ValeConsumoServiceImpl implements ValeConsumoService {

    private static final String REGISTRADO = "REGISTRADO";
    private static final String ANULADO = "ANULADO";
    private static final String SALIDA_VALE = "SALIDA_VALE";
    private static final String AJUSTE_SALIDA = "AJUSTE_SALIDA";
    private static final String AJUSTE_ENTRADA = "AJUSTE_ENTRADA";
    private static final String ANULACION_ENTRADA = "ANULACION_ENTRADA";
    private static final BigDecimal ZERO = BigDecimal.ZERO.setScale(4);

    private final ValeConsumoRepository valeConsumoRepository;
    private final CentroCostoRepository centroCostoRepository;
    private final ProductoRepository productoRepository;
    private final DestinoRepository destinoRepository;
    private final ParteEquipoRepository parteEquipoRepository;
    private final UnidadMedidaRepository unidadMedidaRepository;
    private final KardexMovimientoRepository kardexMovimientoRepository;

    @Override
    @Transactional(readOnly = true)
    public CustomPage<ValeConsumoListadoResponse> listar(String busqueda, Integer centroCostoId,
                                                         String estado, LocalDate fechaDesde,
                                                         LocalDate fechaHasta, Pageable pageable) {
        Page<ValeConsumoListadoResponse> page = valeConsumoRepository.listarResumen(
                        busqueda == null ? "" : busqueda.trim(), centroCostoId,
                        estado == null ? "" : estado.trim(), fechaDesde, fechaHasta, pageable)
                .map(this::convertirListado);
        return new CustomPage<>(page);
    }

    private ValeConsumoListadoResponse convertirListado(ValeConsumoListadoProjection projection) {
        return new ValeConsumoListadoResponse(
                projection.getId(), projection.getNumeroVale(), projection.getFechaVale(),
                projection.getCentroCosto(), projection.getSolicitante(), projection.getProductos(),
                projection.getDestinos(), projection.getCantidadTotal(), projection.getTotal(),
                projection.getEstado());
    }

    @Override
    @Transactional(readOnly = true)
    public ValeConsumoResponse obtener(Integer id) {
        return convertir(obtenerVale(id));
    }

    @Override
    @Transactional
    public ValeConsumoResponse crear(ValeConsumoRequest request) {
        CentroCostoEntity centroCosto = obtenerCentroCostoActivo(request.centroCostoId());
        Map<Integer, ProductoEntity> productos = validarDetalleYCatalogos(request);
        Map<Integer, BigDecimal> cantidades = totalizar(request.detalles());

        for (Map.Entry<Integer, BigDecimal> entrada : cantidades.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()).toList()) {
            ProductoEntity producto = obtenerProductoActivo(entrada.getKey());
            BigDecimal stock = normalizar(producto.getStockActual());
            if (stock.compareTo(entrada.getValue()) < 0) {
                throw stockInsuficiente(producto, entrada.getValue(), stock);
            }
        }

        Map<Integer, BigDecimal> saldosAnteriores = new HashMap<>();
        for (Map.Entry<Integer, BigDecimal> entrada : cantidades.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()).toList()) {
            if (productoRepository.descontarStockDisponible(entrada.getKey(), entrada.getValue()) != 1) {
                ProductoEntity producto = obtenerProductoActivo(entrada.getKey());
                throw stockInsuficiente(producto, entrada.getValue(), normalizar(producto.getStockActual()));
            }
            BigDecimal saldoPosterior = normalizar(obtenerProductoExistente(entrada.getKey()).getStockActual());
            saldosAnteriores.put(entrada.getKey(), saldoPosterior.add(entrada.getValue()));
        }

        ValeConsumoEntity vale = new ValeConsumoEntity();
        vale.setNumeroVale("P-" + UUID.randomUUID().toString().replace("-", "").substring(0, 20));
        vale.setFechaVale(request.fechaVale());
        vale.setCentroCostoId(centroCosto.getCentroCostoId());
        vale.setSolicitante(request.solicitante().trim());
        vale.setMotivo(request.motivo().trim());
        vale.setEstado(REGISTRADO);
        agregarDetalles(vale, request, productos);
        vale = valeConsumoRepository.saveAndFlush(vale);
        vale.setNumeroVale(generarNumeroVale(vale));
        vale = valeConsumoRepository.saveAndFlush(vale);

        List<KardexMovimientoEntity> movimientos = new ArrayList<>();
        for (DetalleValeConsumoRequest detalle : request.detalles()) {
            ProductoEntity producto = productos.get(detalle.productoId());
            BigDecimal saldo = saldosAnteriores.get(producto.getProductoId());
            for (DistribucionValeConsumoRequest distribucion : detalle.distribuciones()) {
                BigDecimal cantidad = normalizar(distribucion.cantidad());
                BigDecimal saldoNuevo = saldo.subtract(cantidad);
                movimientos.add(crearMovimiento(
                        vale, producto, SALIDA_VALE, cantidad, saldo, saldoNuevo,
                        distribucion.destinoId(), distribucion.parteEquipoId()
                ));
                saldo = saldoNuevo;
            }
        }
        kardexMovimientoRepository.saveAll(movimientos);
        return convertir(vale);
    }

    @Override
    @Transactional
    public ValeConsumoResponse actualizar(Integer id, ValeConsumoRequest request) {
        ValeConsumoEntity vale = obtenerVale(id);
        validarEditable(vale);
        CentroCostoEntity centroCosto = obtenerCentroCostoActivo(request.centroCostoId());
        Map<Integer, ProductoEntity> productos = validarDetalleYCatalogos(request);

        Map<Integer, BigDecimal> anterior = totalizarExistente(vale);
        Map<Integer, BigDecimal> nuevo = totalizar(request.detalles());
        Map<Integer, BigDecimal> diferencias = diferencias(anterior, nuevo);

        for (Map.Entry<Integer, BigDecimal> entrada : diferencias.entrySet().stream()
                .filter(e -> e.getValue().signum() > 0)
                .sorted(Map.Entry.comparingByKey()).toList()) {
            ProductoEntity producto = obtenerProductoActivo(entrada.getKey());
            BigDecimal disponible = normalizar(producto.getStockActual());
            if (disponible.compareTo(entrada.getValue()) < 0) {
                throw stockInsuficiente(producto, entrada.getValue(), disponible);
            }
        }

        vale.setFechaVale(request.fechaVale());
        vale.setCentroCostoId(centroCosto.getCentroCostoId());
        vale.setSolicitante(request.solicitante().trim());
        vale.setMotivo(request.motivo().trim());
        vale.limpiarDetalles();
        agregarDetalles(vale, request, productos);
        vale = valeConsumoRepository.saveAndFlush(vale);

        List<KardexMovimientoEntity> movimientos = new ArrayList<>();
        for (Map.Entry<Integer, BigDecimal> entrada : diferencias.entrySet().stream()
                .filter(e -> e.getValue().signum() != 0)
                .sorted(Map.Entry.comparingByKey()).toList()) {
            Integer productoId = entrada.getKey();
            BigDecimal diferencia = entrada.getValue();
            ProductoEntity producto = diferencia.signum() > 0
                    ? obtenerProductoActivo(productoId)
                    : obtenerProductoExistente(productoId);
            BigDecimal cantidad = diferencia.abs();
            String tipoMovimiento;
            BigDecimal saldoAnterior;
            BigDecimal saldoNuevo;

            if (diferencia.signum() > 0) {
                if (productoRepository.descontarStockDisponible(productoId, cantidad) != 1) {
                    ProductoEntity actual = obtenerProductoActivo(productoId);
                    throw stockInsuficiente(actual, cantidad, normalizar(actual.getStockActual()));
                }
                tipoMovimiento = AJUSTE_SALIDA;
                saldoNuevo = normalizar(obtenerProductoExistente(productoId).getStockActual());
                saldoAnterior = saldoNuevo.add(cantidad);
            } else {
                if (productoRepository.incrementarStock(productoId, cantidad) != 1) {
                    throw new ValeConsumoConflictException(
                            "No se pudo ajustar el stock del producto " + productoId + ".");
                }
                tipoMovimiento = AJUSTE_ENTRADA;
                saldoNuevo = normalizar(obtenerProductoExistente(productoId).getStockActual());
                saldoAnterior = saldoNuevo.subtract(cantidad);
            }

            BigDecimal precio = producto.getPrecioUnitario() == null
                    ? ZERO
                    : producto.getPrecioUnitario();
            movimientos.add(crearMovimiento(
                    vale, productoId, tipoMovimiento, cantidad, saldoAnterior, saldoNuevo,
                    precio, null, null
            ));
        }
        kardexMovimientoRepository.saveAll(movimientos);
        return convertir(vale);
    }

    @Override
    @Transactional
    public ValeConsumoResponse anular(Integer id) {
        ValeConsumoEntity vale = obtenerVale(id);
        if (ANULADO.equals(vale.getEstado())) {
            throw new ValeConsumoConflictException("El vale ya se encuentra anulado.");
        }

        Map<Integer, BigDecimal> cantidades = totalizarExistente(vale);
        Map<Integer, ProductoEntity> productos = new HashMap<>();
        for (Integer productoId : cantidades.keySet()) {
            productos.put(productoId, obtenerProductoExistente(productoId));
        }

        vale.setEstado(ANULADO);
        vale = valeConsumoRepository.saveAndFlush(vale);

        List<KardexMovimientoEntity> movimientos = new ArrayList<>();
        for (Map.Entry<Integer, BigDecimal> entrada : cantidades.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()).toList()) {
            Integer productoId = entrada.getKey();
            BigDecimal cantidadTotal = entrada.getValue();
            ProductoEntity producto = productos.get(productoId);
            if (productoRepository.incrementarStock(productoId, cantidadTotal) != 1) {
                throw new ValeConsumoConflictException(
                        "No se pudo devolver el stock del producto " + productoId + ".");
            }

            BigDecimal saldoNuevoTotal = normalizar(obtenerProductoExistente(productoId).getStockActual());
            BigDecimal saldo = saldoNuevoTotal.subtract(cantidadTotal);
            BigDecimal precio = producto.getPrecioUnitario() == null ? ZERO : producto.getPrecioUnitario();
            BigDecimal restante = cantidadTotal;
            for (DetalleValeConsumoEntity detalle : vale.getDetalles()) {
                if (!productoId.equals(detalle.getProductoId())) {
                    continue;
                }
                for (DistribucionValeConsumoEntity distribucion : detalle.getDistribuciones()) {
                    BigDecimal cantidad = normalizar(distribucion.getCantidad());
                    BigDecimal saldoNuevo = saldo.add(cantidad);
                    movimientos.add(crearMovimiento(
                            vale, productoId, ANULACION_ENTRADA, cantidad, saldo, saldoNuevo,
                            detalle.getPrecioUnitario() == null ? precio : detalle.getPrecioUnitario(),
                            distribucion.getDestinoId(), distribucion.getParteEquipoId()
                    ));
                    saldo = saldoNuevo;
                    restante = restante.subtract(cantidad);
                }
            }
            if (restante.signum() != 0) {
                throw new IllegalStateException("La cantidad de las distribuciones no coincide con el detalle.");
            }
        }
        kardexMovimientoRepository.saveAll(movimientos);
        return convertir(vale);
    }

    private ValeConsumoEntity obtenerVale(Integer id) {
        return valeConsumoRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("No se encontró el vale de consumo solicitado."));
    }

    private void validarEditable(ValeConsumoEntity vale) {
        if (ANULADO.equals(vale.getEstado())) {
            throw new ValeConsumoConflictException("No se puede editar un vale anulado.");
        }
        if (!REGISTRADO.equals(vale.getEstado())) {
            throw new ValeConsumoConflictException("El vale tiene un estado no permitido.");
        }
    }

    private CentroCostoEntity obtenerCentroCostoActivo(Integer id) {
        CentroCostoEntity centroCosto = centroCostoRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("No se encontró el centro de costo " + id + "."));
        if (!Boolean.TRUE.equals(centroCosto.getActivo())) {
            throw new ValeConsumoConflictException("El centro de costo seleccionado está inactivo.");
        }
        return centroCosto;
    }

    private ProductoEntity obtenerProductoActivo(Integer id) {
        ProductoEntity producto = obtenerProductoExistente(id);
        if (!Boolean.TRUE.equals(producto.getActivo())) {
            throw new ValeConsumoConflictException("El producto " + id + " está inactivo.");
        }
        return producto;
    }

    private ProductoEntity obtenerProductoExistente(Integer id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("No se encontró el producto " + id + "."));
    }

    private Map<Integer, ProductoEntity> validarDetalleYCatalogos(ValeConsumoRequest request) {
        Map<Integer, ProductoEntity> productos = new LinkedHashMap<>();
        Set<Integer> productosVistos = new HashSet<>();
        for (DetalleValeConsumoRequest detalle : request.detalles()) {
            if (!productosVistos.add(detalle.productoId())) {
                throw new ValeConsumoValidationException("No se permite repetir un producto en el vale.");
            }
            ProductoEntity producto = obtenerProductoActivo(detalle.productoId());
            productos.put(producto.getProductoId(), producto);
            Set<DistribucionKey> distribucionesVistas = new HashSet<>();
            for (DistribucionValeConsumoRequest distribucion : detalle.distribuciones()) {
                DestinoEntity destino = destinoRepository.findById(distribucion.destinoId())
                        .orElseThrow(() -> new CustomNotFoundException(
                                "No se encontró el destino " + distribucion.destinoId() + "."));
                if (!Boolean.TRUE.equals(destino.getActivo())) {
                    throw new ValeConsumoConflictException("El destino seleccionado está inactivo.");
                }
                if (distribucion.parteEquipoId() != null) {
                    ParteEquipoEntity parte = parteEquipoRepository.findById(distribucion.parteEquipoId())
                            .orElseThrow(() -> new CustomNotFoundException(
                                    "No se encontró la parte de equipo " + distribucion.parteEquipoId() + "."));
                    if (!Boolean.TRUE.equals(parte.getActivo())) {
                        throw new ValeConsumoConflictException("La parte de equipo seleccionada está inactiva.");
                    }
                }
                DistribucionKey key = new DistribucionKey(
                        distribucion.destinoId(), distribucion.parteEquipoId());
                if (!distribucionesVistas.add(key)) {
                    throw new ValeConsumoValidationException(
                            "No se permite repetir la combinación de destino y parte de equipo.");
                }
            }
        }
        return productos;
    }

    private void agregarDetalles(
            ValeConsumoEntity vale,
            ValeConsumoRequest request,
            Map<Integer, ProductoEntity> productos
    ) {
        for (DetalleValeConsumoRequest detalleRequest : request.detalles()) {
            ProductoEntity producto = productos.get(detalleRequest.productoId());
            DetalleValeConsumoEntity detalle = new DetalleValeConsumoEntity();
            detalle.setProductoId(producto.getProductoId());
            detalle.setPrecioUnitario(normalizar(producto.getPrecioUnitario()));
            for (DistribucionValeConsumoRequest distribucionRequest : detalleRequest.distribuciones()) {
                DistribucionValeConsumoEntity distribucion = new DistribucionValeConsumoEntity();
                distribucion.setDestinoId(distribucionRequest.destinoId());
                distribucion.setParteEquipoId(distribucionRequest.parteEquipoId());
                distribucion.setCantidad(normalizar(distribucionRequest.cantidad()));
                detalle.agregarDistribucion(distribucion);
            }
            vale.agregarDetalle(detalle);
        }
    }

    private Map<Integer, BigDecimal> totalizar(List<DetalleValeConsumoRequest> detalles) {
        Map<Integer, BigDecimal> cantidades = new HashMap<>();
        for (DetalleValeConsumoRequest detalle : detalles) {
            BigDecimal totalDetalle = detalle.distribuciones().stream()
                    .map(DistribucionValeConsumoRequest::cantidad)
                    .map(ValeConsumoServiceImpl::normalizar)
                    .reduce(ZERO, BigDecimal::add);
            cantidades.merge(detalle.productoId(), totalDetalle, BigDecimal::add);
        }
        return cantidades;
    }

    private Map<Integer, BigDecimal> totalizarExistente(ValeConsumoEntity vale) {
        Map<Integer, BigDecimal> cantidades = new HashMap<>();
        for (DetalleValeConsumoEntity detalle : vale.getDetalles()) {
            BigDecimal totalDetalle = detalle.getDistribuciones().stream()
                    .map(DistribucionValeConsumoEntity::getCantidad)
                    .map(ValeConsumoServiceImpl::normalizar)
                    .reduce(ZERO, BigDecimal::add);
            cantidades.merge(detalle.getProductoId(), totalDetalle, BigDecimal::add);
        }
        return cantidades;
    }

    private Map<Integer, BigDecimal> diferencias(
            Map<Integer, BigDecimal> anterior,
            Map<Integer, BigDecimal> nuevo
    ) {
        Map<Integer, BigDecimal> diferencias = new HashMap<>();
        Set<Integer> ids = new HashSet<>(anterior.keySet());
        ids.addAll(nuevo.keySet());
        for (Integer id : ids) {
            diferencias.put(id, normalizar(nuevo.getOrDefault(id, ZERO))
                    .subtract(normalizar(anterior.getOrDefault(id, ZERO))));
        }
        return diferencias;
    }

    private StockInsuficienteException stockInsuficiente(
            ProductoEntity producto,
            BigDecimal solicitado,
            BigDecimal disponible
    ) {
        return new StockInsuficienteException(List.of(
                new StockInsuficienteException.ProductoSinStock(
                        producto.getProductoId(), producto.getNombre(), solicitado, disponible)
        ));
    }

    private String generarNumeroVale(ValeConsumoEntity vale) {
        return String.format(Locale.ROOT, "VAL-%d-%04d", vale.getFechaVale().getYear(), vale.getValeConsumoId());
    }

    private KardexMovimientoEntity crearMovimiento(
            ValeConsumoEntity vale,
            ProductoEntity producto,
            String tipo,
            BigDecimal cantidad,
            BigDecimal saldoAnterior,
            BigDecimal saldoNuevo,
            Integer destinoId,
            Integer parteEquipoId
    ) {
        return crearMovimiento(
                vale, producto.getProductoId(), tipo, cantidad, saldoAnterior, saldoNuevo,
                normalizar(producto.getPrecioUnitario()), destinoId, parteEquipoId
        );
    }

    private KardexMovimientoEntity crearMovimiento(
            ValeConsumoEntity vale,
            Integer productoId,
            String tipo,
            BigDecimal cantidad,
            BigDecimal saldoAnterior,
            BigDecimal saldoNuevo,
            BigDecimal precio,
            Integer destinoId,
            Integer parteEquipoId
    ) {
        KardexMovimientoEntity movimiento = new KardexMovimientoEntity();
        movimiento.setValeConsumoId(vale.getValeConsumoId());
        movimiento.setUsuarioResponsable(usuarioActual());
        movimiento.setProductoId(productoId);
        movimiento.setTipoMovimiento(tipo);
        movimiento.setFechaMovimiento(LocalDateTime.now());
        movimiento.setCantidad(normalizar(cantidad));
        movimiento.setSaldoAnterior(normalizar(saldoAnterior));
        movimiento.setSaldoNuevo(normalizar(saldoNuevo));
        movimiento.setPrecioUnitario(normalizar(precio));
        movimiento.setCentroCostoId(vale.getCentroCostoId());
        movimiento.setDestinoId(destinoId);
        movimiento.setParteEquipoId(parteEquipoId);
        movimiento.setNumeroDocumento(vale.getNumeroVale());
        return movimiento;
    }

    private String usuarioActual() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication == null || authentication.getName() == null
                ? "sistema"
                : authentication.getName();
    }

    private ValeConsumoResponse convertir(ValeConsumoEntity vale) {
        CentroCostoEntity centroCosto = centroCostoRepository.findById(vale.getCentroCostoId())
                .orElseThrow(() -> new CustomNotFoundException("No se encontró el centro de costo del vale."));

        List<DetalleValeConsumoResponse> detalles = new ArrayList<>();
        BigDecimal cantidadTotal = ZERO;
        BigDecimal total = ZERO;
        for (DetalleValeConsumoEntity detalle : vale.getDetalles()) {
            ProductoEntity producto = productoRepository.findById(detalle.getProductoId())
                    .orElseThrow(() -> new CustomNotFoundException(
                            "No se encontró el producto " + detalle.getProductoId() + "."));
            BigDecimal cantidad = detalle.getDistribuciones().stream()
                    .map(DistribucionValeConsumoEntity::getCantidad)
                    .map(ValeConsumoServiceImpl::normalizar)
                    .reduce(ZERO, BigDecimal::add);
            BigDecimal precio = normalizar(detalle.getPrecioUnitario());
            BigDecimal subtotal = precio.multiply(cantidad);
            cantidadTotal = cantidadTotal.add(cantidad);
            total = total.add(subtotal);

            List<DistribucionValeConsumoResponse> distribuciones = detalle.getDistribuciones().stream()
                    .map(this::convertirDistribucion)
                    .toList();
            String unidad = unidadMedidaRepository.findById(producto.getUnidadMedidaId())
                    .map(unidadMedida -> unidadMedida.getNombre())
                    .orElse(null);
            detalles.add(new DetalleValeConsumoResponse(
                    detalle.getDetalleValeConsumoId(),
                    producto.getProductoId(),
                    producto.getCodigo(),
                    producto.getNombre(),
                    producto.getUnidadMedidaId(),
                    unidad,
                    precio,
                    normalizar(producto.getStockActual()),
                    cantidad,
                    subtotal,
                    distribuciones
            ));
        }

        List<KardexMovimientoResponse> movimientos = kardexMovimientoRepository
                .findByValeConsumoIdOrderByKardexMovimientoIdAsc(vale.getValeConsumoId())
                .stream()
                .map(movimiento -> new KardexMovimientoResponse(
                        movimiento.getKardexMovimientoId(),
                        movimiento.getProductoId(),
                        productoRepository.findById(movimiento.getProductoId())
                            .map(ProductoEntity::getCodigo).orElse(null),
                        productoRepository.findById(movimiento.getProductoId())
                            .map(ProductoEntity::getNombre).orElse(null),
                        movimiento.getTipoMovimiento(),
                        movimiento.getFechaMovimiento(),
                        movimiento.getCantidad(),
                        movimiento.getSaldoAnterior(),
                        movimiento.getSaldoNuevo(),
                        movimiento.getPrecioUnitario(),
                        movimiento.getUsuarioResponsable(),
                        movimiento.getNumeroDocumento(),
                        movimiento.getIngresoAlmacenId(),
                        movimiento.getValeConsumoId(),
                        movimiento.getCentroCostoId(),
                        movimiento.getDestinoId(),
                        movimiento.getParteEquipoId()
                ))
                .toList();

        return new ValeConsumoResponse(
                vale.getValeConsumoId(),
                vale.getNumeroVale(),
                vale.getFechaVale(),
                vale.getCentroCostoId(),
                centroCosto.getNombre(),
                vale.getSolicitante(),
                vale.getMotivo(),
                vale.getEstado(),
                vale.getFechaRegistro(),
                detalles,
                cantidadTotal,
                total,
                movimientos
        );
    }

    private DistribucionValeConsumoResponse convertirDistribucion(DistribucionValeConsumoEntity distribucion) {
        String destino = destinoRepository.findById(distribucion.getDestinoId())
                .map(DestinoEntity::getNombre)
                .orElse(null);
        String parteEquipo = distribucion.getParteEquipoId() == null
                ? null
                : parteEquipoRepository.findById(distribucion.getParteEquipoId())
                .map(ParteEquipoEntity::getNombre)
                .orElse(null);
        return new DistribucionValeConsumoResponse(
                distribucion.getDistribucionValeConsumoId(),
                distribucion.getDestinoId(),
                destino,
                distribucion.getParteEquipoId(),
                parteEquipo,
                normalizar(distribucion.getCantidad())
        );
    }

    private static BigDecimal normalizar(BigDecimal valor) {
        return valor == null ? ZERO : valor.setScale(4, java.math.RoundingMode.UNNECESSARY);
    }

    private record DistribucionKey(Integer destinoId, Integer parteEquipoId) {
    }
}
