package com.agrihusac.SysInvent.service.reporte.impl;

import com.agrihusac.SysInvent.model.response.reporte.ReporteFiltrosResponse;
import com.agrihusac.SysInvent.model.response.reporte.ReporteFiltrosResponse.Opcion;
import com.agrihusac.SysInvent.model.response.reporte.ReporteFiltrosResponse.ProductoOpcion;
import com.agrihusac.SysInvent.repository.CategoriaRepository;
import com.agrihusac.SysInvent.repository.CentroCostoRepository;
import com.agrihusac.SysInvent.repository.DestinoRepository;
import com.agrihusac.SysInvent.repository.ParteEquipoRepository;
import com.agrihusac.SysInvent.repository.ProductoRepository;
import com.agrihusac.SysInvent.repository.ProveedorRepository;
import com.agrihusac.SysInvent.repository.TipoProductoRepository;
import com.agrihusac.SysInvent.service.reporte.ReporteFiltrosService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Entrega en una sola consulta los catálogos activos que necesitan los filtros
 * de los reportes, evitando depender de cachés locales del frontend.
 */
@Service
@RequiredArgsConstructor
public class ReporteFiltrosServiceImpl implements ReporteFiltrosService {

    private final ProveedorRepository proveedorRepository;
    private final CentroCostoRepository centroCostoRepository;
    private final DestinoRepository destinoRepository;
    private final ParteEquipoRepository parteEquipoRepository;
    private final TipoProductoRepository tipoProductoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;

    @Override
    @Transactional(readOnly = true)
    public ReporteFiltrosResponse obtener() {
        return new ReporteFiltrosResponse(
                proveedorRepository.findAllByActivoTrueOrderByRazonSocialAsc().stream()
                        .map(item -> new Opcion(item.getProveedorId(), item.getRuc(), item.getRazonSocial()))
                        .toList(),
                centroCostoRepository.findAllByActivoTrueOrderByNombreAsc().stream()
                        .map(item -> new Opcion(item.getCentroCostoId(), null, item.getNombre()))
                        .toList(),
                destinoRepository.findAllByActivoTrueOrderByNombreAsc().stream()
                        .map(item -> new Opcion(item.getDestinoId(), null, item.getNombre()))
                        .toList(),
                parteEquipoRepository.findAllByActivoTrueOrderByNombreAsc().stream()
                        .map(item -> new Opcion(item.getParteEquipoId(), item.getCodigo(), item.getNombre()))
                        .toList(),
                tipoProductoRepository.findAllByActivoTrueOrderByNombreAsc().stream()
                        .map(item -> new Opcion(item.getTipoProductoId(), null, item.getNombre()))
                        .toList(),
                categoriaRepository.findAllByActivoTrueOrderByNombreAsc().stream()
                        .map(item -> new Opcion(item.getCategoriaId(), null, item.getNombre()))
                        .toList(),
                productoRepository.findAllConUnidadMedida().stream()
                        .filter(item -> Boolean.TRUE.equals(item.getActivo()))
                        .map(item -> new ProductoOpcion(
                                item.getProductoId(), item.getCodigo(), item.getNombre(),
                                item.getTipoProductoId(), item.getCategoriaId(),
                                item.getUnidadMedida() == null ? null : item.getUnidadMedida().getNombre()))
                        .toList());
    }
}
