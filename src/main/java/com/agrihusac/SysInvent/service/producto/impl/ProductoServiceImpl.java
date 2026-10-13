package com.agrihusac.SysInvent.service.producto.impl;

import com.agrihusac.SysInvent.model.entity.ProductoEntity;
import com.agrihusac.SysInvent.model.request.producto.ProductoRequest;
import com.agrihusac.SysInvent.model.response.producto.ProductoResponse;
import com.agrihusac.SysInvent.repository.CategoriaRepository;
import com.agrihusac.SysInvent.repository.ProductoRepository;
import com.agrihusac.SysInvent.repository.ProveedorRepository;
import com.agrihusac.SysInvent.repository.TipoProductoRepository;
import com.agrihusac.SysInvent.repository.UnidadMedidaRepository;
import com.agrihusac.SysInvent.service.catalogo.AbstractCatalogoService;
import com.agrihusac.SysInvent.service.producto.ProductoService;
import com.agrihusac.SysInvent.utils.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ProductoServiceImpl extends AbstractCatalogoService<ProductoEntity, ProductoRequest, ProductoResponse> implements ProductoService {
    private final ProductoRepository repository;
    private final TipoProductoRepository tipoProductoRepository;
    private final CategoriaRepository categoriaRepository;
    private final UnidadMedidaRepository unidadMedidaRepository;
    private final ProveedorRepository proveedorRepository;

    public ProductoServiceImpl(ProductoRepository repository,
            TipoProductoRepository tipoProductoRepository,
            CategoriaRepository categoriaRepository,
            UnidadMedidaRepository unidadMedidaRepository,
            ProveedorRepository proveedorRepository) {
        super(repository);
        this.repository = repository;
        this.tipoProductoRepository = tipoProductoRepository;
        this.categoriaRepository = categoriaRepository;
        this.unidadMedidaRepository = unidadMedidaRepository;
        this.proveedorRepository = proveedorRepository;
    }

    @Override
    protected ProductoEntity crearEntidad(ProductoRequest request) {
        ProductoEntity entity = new ProductoEntity();
        aplicar(entity, request);
        entity.setActivo(true);
        return entity;
    }

    @Override
    protected void actualizarEntidad(ProductoEntity entity, ProductoRequest request) {
        aplicar(entity, request);
    }

    private void aplicar(ProductoEntity entity, ProductoRequest request) {
        entity.setCodigo(request.codigo());
        entity.setNombre(request.nombre());
        entity.setDescripcion(request.descripcion());
        entity.setTipoProducto(tipoProductoRepository.getReferenceById(request.tipoProductoId()));
        entity.setCategoria(categoriaRepository.getReferenceById(request.categoriaId()));
        entity.setUnidadMedida(unidadMedidaRepository.getReferenceById(request.unidadMedidaId()));
        entity.setProveedor(proveedorRepository.getReferenceById(request.proveedorId()));
        entity.setStockActual(request.stockActual());
        entity.setStockMinimo(request.stockMinimo());
        entity.setPrecioUnitario(request.precioUnitario());
    }

    @Override
    protected ProductoResponse respuesta(ProductoEntity entity) {
        return new ProductoResponse(
                entity.getProductoId(),
                entity.getCodigo(),
                entity.getNombre(),
                entity.getDescripcion(),
                entity.getTipoProducto().getTipoProductoId(),
                entity.getCategoria().getCategoriaId(),
                entity.getUnidadMedida().getUnidadMedidaId(),
                entity.getProveedor().getProveedorId(),
                entity.getStockActual(),
                entity.getStockMinimo(),
                entity.getPrecioUnitario(),
                entity.getActivo(),
                entity.getFechaRegistro());
    }

    @Override
    protected boolean duplicado(ProductoRequest request, Integer idExcluido) {
        return idExcluido == null
                ? repository.existsByCodigoIgnoreCase(request.codigo())
                : repository.existsByCodigoIgnoreCaseAndProductoIdNot(request.codigo(), idExcluido);
    }

    @Override
    protected Page<ProductoEntity> buscarActivos(String filtro, Pageable pageable) {
        return repository.findByActivoTrueAndNombreContainingIgnoreCase(filtro, pageable);
    }

    @Override
    protected String nombreCatalogo() { return "Producto"; }

    @Override
    protected void establecerActivo(ProductoEntity entity, boolean activo) { entity.setActivo(activo); }

    @Override
    protected boolean estaActivo(ProductoEntity entity) { return Boolean.TRUE.equals(entity.getActivo()); }

    @Override
    protected ResponseEntity<Object> validar(ProductoRequest request) {
        if (tipoProductoRepository.findById(request.tipoProductoId())
                .filter(t -> Boolean.TRUE.equals(t.getActivo())).isEmpty()) {
            return referenciaInvalida("No se encontro un tipo de producto activo con ese id");
        }
        if (categoriaRepository.findById(request.categoriaId())
                .filter(c -> Boolean.TRUE.equals(c.getActivo())).isEmpty()) {
            return referenciaInvalida("No se encontro una categoria activa con ese id");
        }
        if (unidadMedidaRepository.findById(request.unidadMedidaId())
                .filter(u -> Boolean.TRUE.equals(u.getActivo())).isEmpty()) {
            return referenciaInvalida("No se encontro una unidad de medida activa con ese id");
        }
        if (proveedorRepository.findById(request.proveedorId())
                .filter(p -> Boolean.TRUE.equals(p.getActivo())).isEmpty()) {
            return referenciaInvalida("No se encontro un proveedor activo con ese id");
        }
        return null;
    }

    private ResponseEntity<Object> referenciaInvalida(String mensaje) {
        return MessageResponse.setResponse(false, HttpStatus.NOT_FOUND, mensaje);
    }
}
