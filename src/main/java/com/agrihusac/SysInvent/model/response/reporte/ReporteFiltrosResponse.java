package com.agrihusac.SysInvent.model.response.reporte;

import java.util.List;

/**
 * Catálogos activos usados por los filtros de los reportes.
 */
public record ReporteFiltrosResponse(
        List<Opcion> proveedores,
        List<Opcion> centrosCosto,
        List<Opcion> destinos,
        List<Opcion> partesEquipo,
        List<Opcion> tiposProducto,
        List<Opcion> categorias,
        List<ProductoOpcion> productos
) {

    public record Opcion(Integer id, String codigo, String nombre) {
    }

    public record ProductoOpcion(
            Integer id,
            String codigo,
            String nombre,
            Integer tipoProductoId,
            Integer categoriaId,
            String unidadMedida
    ) {
    }
}
