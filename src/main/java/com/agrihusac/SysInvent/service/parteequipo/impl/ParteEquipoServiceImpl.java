package com.agrihusac.SysInvent.service.parteequipo.impl;

import com.agrihusac.SysInvent.model.entity.ParteEquipoEntity;
import com.agrihusac.SysInvent.model.request.parteequipo.ParteEquipoRequest;
import com.agrihusac.SysInvent.model.response.parteequipo.ParteEquipoResponse;
import com.agrihusac.SysInvent.repository.ParteEquipoRepository;
import com.agrihusac.SysInvent.service.catalogo.AbstractCatalogoService;
import com.agrihusac.SysInvent.service.parteequipo.ParteEquipoService;
import com.agrihusac.SysInvent.utils.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ParteEquipoServiceImpl extends AbstractCatalogoService<ParteEquipoEntity, ParteEquipoRequest, ParteEquipoResponse> implements ParteEquipoService {
    private final ParteEquipoRepository repository;

    public ParteEquipoServiceImpl(ParteEquipoRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    protected ParteEquipoEntity crearEntidad(ParteEquipoRequest request) {
        ParteEquipoEntity entity = new ParteEquipoEntity();
        entity.setCodigo(request.codigo());
        entity.setNombre(request.nombre());
        entity.setDescripcion(request.descripcion());
        entity.setActivo(true);
        return entity;
    }

    @Override
    protected void actualizarEntidad(ParteEquipoEntity entity, ParteEquipoRequest request) {
        entity.setCodigo(request.codigo());
        entity.setNombre(request.nombre());
        entity.setDescripcion(request.descripcion());
    }

    @Override
    protected ParteEquipoResponse respuesta(ParteEquipoEntity entity) {
        return new ParteEquipoResponse(entity.getParteEquipoId(), entity.getCodigo(), entity.getNombre(), entity.getDescripcion(), entity.getActivo(), entity.getFechaRegistro());
    }

    @Override
    protected boolean duplicado(ParteEquipoRequest request, Integer idExcluido) {
        return idExcluido == null ? repository.existsByCodigoIgnoreCase(request.codigo()) : repository.existsByCodigoIgnoreCaseAndParteEquipoIdNot(request.codigo(), idExcluido);
    }

    @Override
    protected Page<ParteEquipoEntity> buscarActivos(String filtro, Pageable pageable) {
        return repository.findByActivoTrueAndNombreContainingIgnoreCase(filtro, pageable);
    }

    @Override
    protected String nombreCatalogo() { return "Parte de equipo"; }

    @Override
    protected void establecerActivo(ParteEquipoEntity entity, boolean activo) { entity.setActivo(activo); }

    @Override
    protected boolean estaActivo(ParteEquipoEntity entity) { return Boolean.TRUE.equals(entity.getActivo()); }

    @Override
    protected ResponseEntity<Object> validar(ParteEquipoRequest request) {
        return null;
    }
}
