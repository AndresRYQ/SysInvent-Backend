package com.agrihusac.SysInvent.service.dashboard.impl;

import com.agrihusac.SysInvent.model.projection.DashboardActividadProjection;
import com.agrihusac.SysInvent.model.projection.DashboardResumenProjection;
import com.agrihusac.SysInvent.model.response.dashboard.DashboardResponse;
import com.agrihusac.SysInvent.repository.DashboardRepository;
import com.agrihusac.SysInvent.service.dashboard.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final DashboardRepository dashboardRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardResponse obtener() {
        DashboardResumenProjection resumen = dashboardRepository.resumen();
        List<DashboardActividadProjection> actividades = dashboardRepository.actividadReciente();

        List<DashboardResponse.Alerta> alertas = new ArrayList<>();
        if (resumen.getStockCritico() > 0) {
            alertas.add(new DashboardResponse.Alerta(
                    "WARNING", "Productos con stock bajo",
                    resumen.getStockCritico() + " productos requieren reposición."));
        }
        if (resumen.getProductosSinStock() > 0) {
            alertas.add(new DashboardResponse.Alerta(
                    "DANGER", "Productos sin stock",
                    resumen.getProductosSinStock() + " producto(s) no tienen stock disponible."));
        }
        if (!actividades.isEmpty()) {
            DashboardActividadProjection ultima = actividades.get(0);
            alertas.add(new DashboardResponse.Alerta(
                    "INFO", "Último movimiento",
                    ultima.getDocumento() + " registrado el " + ultima.getFecha() + "."));
        }

        List<DashboardResponse.ActividadReciente> actividadResponse = actividades.stream()
                .map(item -> new DashboardResponse.ActividadReciente(
                        item.getFecha(), item.getTipo(), item.getDocumento(), item.getProducto(),
                        item.getCantidad(), item.getResponsable()))
                .toList();

        return new DashboardResponse(
                new DashboardResponse.Cards(
                        resumen.getProductosActivos(), resumen.getStockCritico(),
                        resumen.getIngresosMes(), resumen.getValesMes(), resumen.getMovimientosHoy()),
                alertas,
                actividadResponse);
    }
}
