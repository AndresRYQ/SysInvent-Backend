package com.agrihusac.SysInvent.service.tipodocumento.impl;

import com.agrihusac.SysInvent.model.entity.TipoDocumentoEntity;
import com.agrihusac.SysInvent.model.request.tipodocumento.TipoDocumentoRequest;
import com.agrihusac.SysInvent.model.response.tipodocumento.TipoDocumentoResponse;
import com.agrihusac.SysInvent.repository.TipoDocumentoRepository;
import com.agrihusac.SysInvent.service.catalogo.AbstractCatalogoService;
import com.agrihusac.SysInvent.service.tipodocumento.TipoDocumentoService;
import com.agrihusac.SysInvent.utils.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TipoDocumentoServiceImpl extends AbstractCatalogoService<TipoDocumentoEntity, TipoDocumentoRequest, TipoDocumentoResponse> implements TipoDocumentoService {
    private final TipoDocumentoRepository repository;

    public TipoDocumentoServiceImpl(TipoDocumentoRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    protected TipoDocumentoEntity crearEntidad(TipoDocumentoRequest request) {
        TipoDocumentoEntity entity = new TipoDocumentoEntity();
        entity.setNombre(request.nombre());
        entity.setDescripcion(request.descripcion());
        entity.setActivo(true);
        return entity;
    }

    @Override
    protected void actualizarEntidad(TipoDocumentoEntity entity, TipoDocumentoRequest request) {
        entity.setNombre(request.nombre());
        entity.setDescripcion(request.descripcion());
    }

    @Override
    protected TipoDocumentoResponse respuesta(TipoDocumentoEntity entity) {
        return new TipoDocumentoResponse(entity.getTipoDocumentoId(), entity.getNombre(), entity.getDescripcion(), entity.getActivo(), entity.getFechaRegistro());
    }

    @Override
    protected boolean duplicado(TipoDocumentoRequest request, Integer idExcluido) {
        return idExcluido == null ? repository.existsByNombreIgnoreCase(request.nombre()) : repository.existsByNombreIgnoreCaseAndTipoDocumentoIdNot(request.nombre(), idExcluido);
    }

    @Override
    protected Page<TipoDocumentoEntity> buscarActivos(String filtro, Pageable pageable) {
        return repository.findByActivoTrueAndNombreContainingIgnoreCase(filtro, pageable);
    }

    @Override
    protected String nombreCatalogo() { return "Tipo de documento"; }

    @Override
    protected void establecerActivo(TipoDocumentoEntity entity, boolean activo) { entity.setActivo(activo); }

    @Override
    protected boolean estaActivo(TipoDocumentoEntity entity) { return Boolean.TRUE.equals(entity.getActivo()); }

    @Override
    protected ResponseEntity<Object> validar(TipoDocumentoRequest request) {
        return null;
    }
}
