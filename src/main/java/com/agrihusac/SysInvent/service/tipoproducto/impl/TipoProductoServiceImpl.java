package com.agrihusac.SysInvent.service.tipoproducto.impl;

import com.agrihusac.SysInvent.model.entity.TipoProductoEntity;
import com.agrihusac.SysInvent.model.request.tipoproducto.TipoProductoRequest;
import com.agrihusac.SysInvent.model.response.tipoproducto.TipoProductoResponse;
import com.agrihusac.SysInvent.repository.TipoProductoRepository;
import com.agrihusac.SysInvent.service.catalogo.AbstractCatalogoService;
import com.agrihusac.SysInvent.service.tipoproducto.TipoProductoService;
import com.agrihusac.SysInvent.utils.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TipoProductoServiceImpl extends AbstractCatalogoService<TipoProductoEntity, TipoProductoRequest, TipoProductoResponse> implements TipoProductoService {
    private final TipoProductoRepository repository;

    public TipoProductoServiceImpl(TipoProductoRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    protected TipoProductoEntity crearEntidad(TipoProductoRequest request) {
        TipoProductoEntity entity = new TipoProductoEntity();
        entity.setNombre(request.nombre());
        entity.setDescripcion(request.descripcion());
        entity.setActivo(true);
        return entity;
    }

    @Override
    protected void actualizarEntidad(TipoProductoEntity entity, TipoProductoRequest request) {
        entity.setNombre(request.nombre());
        entity.setDescripcion(request.descripcion());
    }

    @Override
    protected TipoProductoResponse respuesta(TipoProductoEntity entity) {
        return new TipoProductoResponse(entity.getTipoProductoId(), entity.getNombre(), entity.getDescripcion(), entity.getActivo(), entity.getFechaRegistro());
    }

    @Override
    protected boolean duplicado(TipoProductoRequest request, Integer idExcluido) {
        return idExcluido == null ? repository.existsByNombreIgnoreCase(request.nombre()) : repository.existsByNombreIgnoreCaseAndTipoProductoIdNot(request.nombre(), idExcluido);
    }

    @Override
    protected Page<TipoProductoEntity> buscarActivos(String filtro, Pageable pageable) {
        return repository.findByActivoTrueAndNombreContainingIgnoreCase(filtro, pageable);
    }

    @Override
    protected String nombreCatalogo() { return "Tipo de producto"; }

    @Override
    protected void establecerActivo(TipoProductoEntity entity, boolean activo) { entity.setActivo(activo); }

    @Override
    protected boolean estaActivo(TipoProductoEntity entity) { return Boolean.TRUE.equals(entity.getActivo()); }

    @Override
    protected ResponseEntity<Object> validar(TipoProductoRequest request) {
        return null;
    }
}
