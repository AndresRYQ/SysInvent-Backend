package com.agrihusac.SysInvent.api;

import com.agrihusac.SysInvent.model.response.ComunResponse;
import com.agrihusac.SysInvent.repository.CategoriaRepository;
import com.agrihusac.SysInvent.repository.CentroCostoRepository;
import com.agrihusac.SysInvent.repository.DestinoRepository;
import com.agrihusac.SysInvent.repository.ParteEquipoRepository;
import com.agrihusac.SysInvent.repository.ProductoRepository;
import com.agrihusac.SysInvent.repository.ProveedorRepository;
import com.agrihusac.SysInvent.repository.RolRepository;
import com.agrihusac.SysInvent.repository.TipoDocumentoRepository;
import com.agrihusac.SysInvent.repository.TipoProductoRepository;
import com.agrihusac.SysInvent.repository.UnidadMedidaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comun")
@Tag(name = "Combos comunes", description = "Listas activas para combos")
public class ComunController {

    private final CategoriaRepository categoriaRepository;
    private final CentroCostoRepository centroCostoRepository;
    private final DestinoRepository destinoRepository;
    private final ParteEquipoRepository parteEquipoRepository;
    private final ProductoRepository productoRepository;
    private final ProveedorRepository proveedorRepository;
    private final RolRepository rolRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final TipoProductoRepository tipoProductoRepository;
    private final UnidadMedidaRepository unidadMedidaRepository;

    public ComunController(
            CategoriaRepository categoriaRepository,
            CentroCostoRepository centroCostoRepository,
            DestinoRepository destinoRepository,
            ParteEquipoRepository parteEquipoRepository,
            ProductoRepository productoRepository,
            ProveedorRepository proveedorRepository,
            RolRepository rolRepository,
            TipoDocumentoRepository tipoDocumentoRepository,
            TipoProductoRepository tipoProductoRepository,
            UnidadMedidaRepository unidadMedidaRepository) {
        this.categoriaRepository = categoriaRepository;
        this.centroCostoRepository = centroCostoRepository;
        this.destinoRepository = destinoRepository;
        this.parteEquipoRepository = parteEquipoRepository;
        this.productoRepository = productoRepository;
        this.proveedorRepository = proveedorRepository;
        this.rolRepository = rolRepository;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.tipoProductoRepository = tipoProductoRepository;
        this.unidadMedidaRepository = unidadMedidaRepository;
    }

    @GetMapping("/tipos-producto")
    @Operation(summary = "Listar tipos de producto activos")
    public List<ComunResponse> tiposProducto() {
        return tipoProductoRepository.findAllByActivoTrueOrderByNombreAsc().stream()
                .map(item -> new ComunResponse(item.getTipoProductoId(), item.getNombre()))
                .toList();
    }

    @GetMapping("/categorias")
    @Operation(summary = "Listar categorías activas")
    public List<ComunResponse> categorias() {
        return categoriaRepository.findAllByActivoTrueOrderByNombreAsc().stream()
                .map(item -> new ComunResponse(item.getCategoriaId(), item.getNombre()))
                .toList();
    }

    @GetMapping("/unidades-medida")
    @Operation(summary = "Listar unidades de medida activas")
    public List<ComunResponse> unidadesMedida() {
        return unidadMedidaRepository.findAllByActivoTrueOrderByNombreAsc().stream()
                .map(item -> new ComunResponse(item.getUnidadMedidaId(), item.getNombre()))
                .toList();
    }

    @GetMapping("/proveedores")
    @Operation(summary = "Listar proveedores activos")
    public List<ComunResponse> proveedores() {
        return proveedorRepository.findAllByActivoTrueOrderByRazonSocialAsc().stream()
                .map(item -> new ComunResponse(item.getProveedorId(), item.getRazonSocial()))
                .toList();
    }

    @GetMapping("/roles")
    @Operation(summary = "Listar roles activos")
    public List<ComunResponse> roles() {
        return rolRepository.findAllByActivoTrueOrderByNombreAsc().stream()
                .map(item -> new ComunResponse(item.getRolId(), item.getNombre()))
                .toList();
    }

    @GetMapping("/centros-costo")
    @Operation(summary = "Listar centros de costo activos")
    public List<ComunResponse> centrosCosto() {
        return centroCostoRepository.findAllByActivoTrueOrderByNombreAsc().stream()
                .map(item -> new ComunResponse(item.getCentroCostoId(), item.getNombre()))
                .toList();
    }

    @GetMapping("/productos")
    @Operation(summary = "Listar productos activos")
    public List<ComunResponse> productos() {
        return productoRepository.findAllByActivoTrueOrderByNombreAsc().stream()
                .map(item -> new ComunResponse(item.getProductoId(), item.getNombre()))
                .toList();
    }

    @GetMapping("/destinos")
    @Operation(summary = "Listar destinos activos")
    public List<ComunResponse> destinos() {
        return destinoRepository.findAllByActivoTrueOrderByNombreAsc().stream()
                .map(item -> new ComunResponse(item.getDestinoId(), item.getNombre()))
                .toList();
    }

    @GetMapping("/partes-equipo")
    @Operation(summary = "Listar partes de equipo activas")
    public List<ComunResponse> partesEquipo() {
        return parteEquipoRepository.findAllByActivoTrueOrderByNombreAsc().stream()
                .map(item -> new ComunResponse(item.getParteEquipoId(), item.getNombre()))
                .toList();
    }

    @GetMapping("/tipos-documento")
    @Operation(summary = "Listar tipos de documento activos")
    public List<ComunResponse> tiposDocumento() {
        return tipoDocumentoRepository.findAllByActivoTrueOrderByNombreAsc().stream()
                .map(item -> new ComunResponse(item.getTipoDocumentoId(), item.getNombre()))
                .toList();
    }
}
