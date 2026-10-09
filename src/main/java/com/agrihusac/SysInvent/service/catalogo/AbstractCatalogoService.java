package com.agrihusac.SysInvent.service.catalogo;

import com.agrihusac.SysInvent.model.entity.AuditoriaEntity;
import com.agrihusac.SysInvent.utils.CustomPage;
import com.agrihusac.SysInvent.utils.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
public abstract class AbstractCatalogoService<E extends AuditoriaEntity, Q, R>
        implements CatalogoService<Q, R> {

    private final JpaRepository<E, Integer> repository;

    protected AbstractCatalogoService(JpaRepository<E, Integer> repository) {
        this.repository = repository;
    }

    protected abstract E crearEntidad(Q request);

    protected abstract void actualizarEntidad(E entity, Q request);

    protected abstract R respuesta(E entity);

    protected abstract boolean duplicado(Q request, Integer idExcluido);

    protected abstract Page<E> buscarActivos(String filtro, Pageable pageable);

    protected abstract String nombreCatalogo();

    protected abstract void establecerActivo(E entity, boolean activo);

    protected abstract boolean estaActivo(E entity);

    protected ResponseEntity<Object> validar(Q request) {
        return null;
    }

    @Override
    @Transactional
    public ResponseEntity<Object> crear(Q request) {
        ResponseEntity<Object> error = validar(request);
        if (error != null) {
            return error;
        }
        if (duplicado(request, null)) {
            return conflicto();
        }
        E entity = repository.saveAndFlush(crearEntidad(request));
        return MessageResponse.setResponse(true, HttpStatus.CREATED,
                nombreCatalogo() + " registrado correctamente", respuesta(entity));
    }

    @Override
    @Transactional
    public ResponseEntity<Object> actualizar(Integer id, Q request) {
        ResponseEntity<Object> error = validar(request);
        if (error != null) {
            return error;
        }
        Optional<E> result = repository.findById(id);
        if (result.isEmpty() || !activo(result.get())) {
            return noEncontrado();
        }
        if (duplicado(request, id)) {
            return conflicto();
        }
        E entity = result.get();
        actualizarEntidad(entity, request);
        entity = repository.saveAndFlush(entity);
        return MessageResponse.setResponse(true, HttpStatus.OK,
                nombreCatalogo() + " actualizado correctamente", respuesta(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtener(Integer id) {
        return repository.findById(id)
                .<ResponseEntity<Object>>map(entity -> MessageResponse.setResponse(
                        true, HttpStatus.OK, "Registro encontrado", respuesta(entity)))
                .orElseGet(this::noEncontrado);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomPage<R> listar(String filtro, Pageable pageable) {
        Page<R> page = buscarActivos(filtro == null ? "" : filtro.trim(), pageable)
                .map(this::respuesta);
        return new CustomPage<>(page);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> cambiarEstado(Integer id, boolean activo) {
        Optional<E> result = repository.findById(id);
        if (result.isEmpty()) {
            return noEncontrado();
        }
        E entity = result.get();
        establecerActivo(entity, activo);
        entity = repository.saveAndFlush(entity);
        String estado = activo ? "activado" : "desactivado";
        return MessageResponse.setResponse(true, HttpStatus.OK,
                nombreCatalogo() + " " + estado + " correctamente", respuesta(entity));
    }

    private boolean activo(E entity) {
        return estaActivo(entity);
    }

    private ResponseEntity<Object> conflicto() {
        return MessageResponse.setResponse(false, HttpStatus.CONFLICT,
                "Ya existe un registro con ese valor único");
    }

    private ResponseEntity<Object> noEncontrado() {
        return MessageResponse.setResponse(false, HttpStatus.NOT_FOUND,
                "No se encontró el registro");
    }
}
