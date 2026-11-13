package com.agrihusac.SysInvent.service.dashboard.impl;

import com.agrihusac.SysInvent.model.projection.DashboardActividadProjection;
import com.agrihusac.SysInvent.model.projection.DashboardResumenProjection;
import com.agrihusac.SysInvent.model.response.dashboard.DashboardResponse;
import com.agrihusac.SysInvent.repository.DashboardRepository;
import com.agrihusac.SysInvent.service.dashboard.DashboardService;
import com.agrihusac.SysInvent.utils.TipoMovimientoKardex;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    static final int MAX_ACTIVIDADES = 5;

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");

    private final DashboardRepository dashboardRepository;
    private final Clock clock;

    @Override
    @Transactional(readOnly = true)
    public DashboardResponse obtener() {
        LocalDate hoy = LocalDate.now(clock);
        LocalDate inicioMes = hoy.withDayOfMonth(1);

        DashboardResumenProjection resumen = dashboardRepository.resumen(
                inicioMes, inicioMes.plusMonths(1),
                hoy.atStartOfDay(), hoy.plusDays(1).atStartOfDay());
        List<DashboardActividadProjection> actividades =
                dashboardRepository.actividadReciente(PageRequest.of(0, MAX_ACTIVIDADES));

        List<DashboardResponse.ActividadReciente> actividadResponse = actividades.stream()
                .map(this::convertirActividad)
                .toList();

        return new DashboardResponse(
                new DashboardResponse.Cards(
                        valor(resumen.getProductosActivos()), valor(resumen.getStockCritico()),
                        valor(resumen.getStockBajo()), valor(resumen.getProductosSinStock()),
                        valor(resumen.getIngresosMes()), valor(resumen.getValesMes()),
                        valor(resumen.getMovimientosHoy())),
                construirAlertas(resumen, actividadResponse),
                actividadResponse);
    }

    private List<DashboardResponse.Alerta> construirAlertas(
            DashboardResumenProjection resumen, List<DashboardResponse.ActividadReciente> actividades) {
        List<DashboardResponse.Alerta> alertas = new ArrayList<>();
        long sinStock = valor(resumen.getProductosSinStock());
        long stockBajo = valor(resumen.getStockBajo());

        if (sinStock > 0) {
            alertas.add(new DashboardResponse.Alerta(
                    "DANGER", "Productos sin stock",
                    sinStock + " producto(s) no tienen stock disponible."));
        }
        if (stockBajo > 0) {
            alertas.add(new DashboardResponse.Alerta(
                    "WARNING", "Productos con stock bajo",
                    stockBajo + " producto(s) están en el stock mínimo o por debajo y requieren reposición."));
        }
        if (!actividades.isEmpty()) {
            DashboardResponse.ActividadReciente ultima = actividades.get(0);
            alertas.add(new DashboardResponse.Alerta(
                    "INFO", "Último movimiento",
                    ultima.getDocumento() + " registrado el " + ultima.getFecha() + "."));
        }
        if (alertas.isEmpty()) {
            alertas.add(new DashboardResponse.Alerta(
                    "INFO", "Inventario sin alertas",
                    "No existen alertas de stock ni movimientos registrados."));
        }
        return alertas;
    }

    private DashboardResponse.ActividadReciente convertirActividad(DashboardActividadProjection item) {
        String tipoMovimiento = item.getTipoMovimiento();
        return new DashboardResponse.ActividadReciente(
                item.getFechaMovimiento() == null ? null : item.getFechaMovimiento().format(FORMATO_FECHA),
                item.getFechaMovimiento() == null ? null : item.getFechaMovimiento().format(FORMATO_HORA),
                TipoMovimientoKardex.esEntrada(tipoMovimiento) ? "Ingreso" : "Vale",
                tipoMovimiento,
                TipoMovimientoKardex.describir(tipoMovimiento),
                item.getDocumento(),
                item.getProducto(),
                item.getCantidad(),
                nombreResponsable(item));
    }

    private String nombreResponsable(DashboardActividadProjection item) {
        String nombre = Stream.of(item.getNombres(), item.getApePaterno(), item.getApeMaterno())
                .filter(parte -> parte != null && !parte.isBlank())
                .map(String::trim)
                .collect(Collectors.joining(" "));
        return nombre.isBlank() ? item.getUsuarioResponsable() : nombre;
    }

    private static long valor(Long numero) {
        return numero == null ? 0L : numero;
    }
}
