package com.agrihusac.SysInvent.service.producto.impl;

import com.agrihusac.SysInvent.model.entity.CategoriaEntity;
import com.agrihusac.SysInvent.model.entity.ProductoEntity;
import com.agrihusac.SysInvent.model.entity.ProveedorEntity;
import com.agrihusac.SysInvent.model.entity.TipoProductoEntity;
import com.agrihusac.SysInvent.model.entity.UnidadMedidaEntity;
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
import com.agrihusac.SysInvent.utils.FechaUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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
        entity.setCodigo(request.codigo().trim());
        entity.setNombre(request.nombre().trim());
        entity.setDescripcion(request.descripcion() == null ? null : request.descripcion().trim());
        entity.setTipoProductoId(request.tipoProductoId());
        entity.setCategoriaId(request.categoriaId());
        entity.setUnidadMedidaId(request.unidadMedidaId());
        entity.setProveedorId(request.proveedorId());
        entity.setStockActual(normalizar(request.stockActual()));
        entity.setStockMinimo(normalizar(request.stockMinimo()));
        entity.setPrecioUnitario(normalizar(request.precioUnitario()));
        entity.setActivo(true);
        return entity;
    }

    @Override
    protected void actualizarEntidad(ProductoEntity entity, ProductoRequest request) {
        entity.setCodigo(request.codigo().trim());
        entity.setNombre(request.nombre().trim());
        entity.setDescripcion(request.descripcion() == null ? null : request.descripcion().trim());
        entity.setTipoProductoId(request.tipoProductoId());
        entity.setCategoriaId(request.categoriaId());
        entity.setUnidadMedidaId(request.unidadMedidaId());
        entity.setProveedorId(request.proveedorId());
        entity.setStockActual(normalizar(request.stockActual()));
        entity.setStockMinimo(normalizar(request.stockMinimo()));
        entity.setPrecioUnitario(normalizar(request.precioUnitario()));
    }

    @Override
    protected ProductoResponse respuesta(ProductoEntity entity) {
        String tipoNombre = tipoProductoRepository.findById(entity.getTipoProductoId())
                .map(TipoProductoEntity::getNombre).orElse(null);
        String catNombre = categoriaRepository.findById(entity.getCategoriaId())
                .map(CategoriaEntity::getNombre).orElse(null);
        String umNombre = unidadMedidaRepository.findById(entity.getUnidadMedidaId())
                .map(UnidadMedidaEntity::getNombre).orElse(null);
        String provRS = proveedorRepository.findById(entity.getProveedorId())
                .map(ProveedorEntity::getRazonSocial).orElse(null);
        return new ProductoResponse(
                entity.getProductoId(),
                entity.getCodigo(),
                entity.getNombre(),
                entity.getDescripcion(),
                entity.getTipoProductoId(),
                tipoNombre,
                entity.getCategoriaId(),
                catNombre,
                entity.getUnidadMedidaId(),
                umNombre,
                entity.getProveedorId(),
                provRS,
                normalizar(entity.getStockActual()),
                normalizar(entity.getStockMinimo()),
                normalizar(entity.getPrecioUnitario()),
                entity.getActivo(),
                FechaUtils.formatear(entity.getFechaRegistro())
        );
    }

    @Override
    protected boolean duplicado(ProductoRequest request, Integer idExcluido) {
        if (idExcluido == null) {
            return repository.existsByCodigoIgnoreCase(request.codigo());
        }
        return repository.existsByCodigoIgnoreCaseAndProductoIdNot(request.codigo(), idExcluido);
    }

    @Override
    protected Page<ProductoEntity> buscarActivos(String filtro, Pageable pageable) {
        return repository.findByActivoTrueAndNombreContainingIgnoreCase(filtro == null ? "" : filtro.trim(), pageable);
    }

    @Override
    protected String nombreCatalogo() {
        return "Producto";
    }

    @Override
    protected void establecerActivo(ProductoEntity entity, boolean activo) {
        entity.setActivo(activo);
    }

    @Override
    protected boolean estaActivo(ProductoEntity entity) {
        return Boolean.TRUE.equals(entity.getActivo());
    }

    @Override
    protected ResponseEntity<MessageResponse> validar(ProductoRequest request) {
        if (tipoProductoRepository.findById(request.tipoProductoId())
                .filter(t -> Boolean.TRUE.equals(t.getActivo())).isEmpty()) {
            return MessageResponse.setResponse(false, HttpStatus.NOT_FOUND, "No se encontró un tipo de producto activo con ese id");
        }
        if (categoriaRepository.findById(request.categoriaId())
                .filter(c -> Boolean.TRUE.equals(c.getActivo())).isEmpty()) {
            return MessageResponse.setResponse(false, HttpStatus.NOT_FOUND, "No se encontró una categoría activa con ese id");
        }
        if (unidadMedidaRepository.findById(request.unidadMedidaId())
                .filter(u -> Boolean.TRUE.equals(u.getActivo())).isEmpty()) {
            return MessageResponse.setResponse(false, HttpStatus.NOT_FOUND, "No se encontró una unidad de medida activa con ese id");
        }
        if (proveedorRepository.findById(request.proveedorId())
                .filter(p -> Boolean.TRUE.equals(p.getActivo())).isEmpty()) {
            return MessageResponse.setResponse(false, HttpStatus.NOT_FOUND, "No se encontró un proveedor activo con ese id");
        }
        return null;
    }

    private BigDecimal normalizar(BigDecimal valor) {
        if (valor == null) return BigDecimal.ZERO.setScale(4);
        return valor.setScale(4, java.math.RoundingMode.UNNECESSARY);
    }
}
