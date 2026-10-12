package com.agrihusac.SysInvent.service.centrocosto.impl;

import com.agrihusac.SysInvent.model.entity.CentroCostoEntity;
import com.agrihusac.SysInvent.model.request.centrocosto.CentroCostoRequest;
import com.agrihusac.SysInvent.model.response.centrocosto.CentroCostoResponse;
import com.agrihusac.SysInvent.repository.CentroCostoRepository;
import com.agrihusac.SysInvent.service.catalogo.AbstractCatalogoService;
import com.agrihusac.SysInvent.service.centrocosto.CentroCostoService;
import com.agrihusac.SysInvent.utils.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CentroCostoServiceImpl extends AbstractCatalogoService<CentroCostoEntity, CentroCostoRequest, CentroCostoResponse> implements CentroCostoService {
    private final CentroCostoRepository repository;

    public CentroCostoServiceImpl(CentroCostoRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    protected CentroCostoEntity crearEntidad(CentroCostoRequest request) {
        CentroCostoEntity entity = new CentroCostoEntity();
        entity.setNombre(request.nombre());
        entity.setDescripcion(request.descripcion());
        entity.setActivo(true);
        return entity;
    }

    @Override
    protected void actualizarEntidad(CentroCostoEntity entity, CentroCostoRequest request) {
        entity.setNombre(request.nombre());
        entity.setDescripcion(request.descripcion());
    }

    @Override
    protected CentroCostoResponse respuesta(CentroCostoEntity entity) {
        return new CentroCostoResponse(entity.getCentroCostoId(), entity.getNombre(), entity.getDescripcion(), entity.getActivo(), entity.getFechaRegistro());
    }

    @Override
    protected boolean duplicado(CentroCostoRequest request, Integer idExcluido) {
        return idExcluido == null ? repository.existsByNombreIgnoreCase(request.nombre()) : repository.existsByNombreIgnoreCaseAndCentroCostoIdNot(request.nombre(), idExcluido);
    }

    @Override
    protected Page<CentroCostoEntity> buscarActivos(String filtro, Pageable pageable) {
        return repository.findByActivoTrueAndNombreContainingIgnoreCase(filtro, pageable);
    }

    @Override
    protected String nombreCatalogo() { return "Centro de costo"; }

    @Override
    protected void establecerActivo(CentroCostoEntity entity, boolean activo) { entity.setActivo(activo); }

    @Override
    protected boolean estaActivo(CentroCostoEntity entity) { return Boolean.TRUE.equals(entity.getActivo()); }

    @Override
    protected ResponseEntity<Object> validar(CentroCostoRequest request) {
        return null;
    }
}
