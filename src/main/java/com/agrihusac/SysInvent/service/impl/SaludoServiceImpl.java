package com.agrihusac.SysInvent.service.impl;

import com.agrihusac.SysInvent.model.entity.SaludoEntity;
import com.agrihusac.SysInvent.model.request.SaludoRequest;
import com.agrihusac.SysInvent.model.response.SaludoResponse;
import com.agrihusac.SysInvent.repository.SaludoRepository;
import com.agrihusac.SysInvent.service.SaludoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaludoServiceImpl implements SaludoService {

    private final SaludoRepository saludoRepository;

    @Override
    public SaludoResponse crearSaludo(SaludoRequest request) {
        SaludoEntity saludo = SaludoEntity.builder()
                .mensaje(request.getMensaje())
                .build();

        SaludoEntity saludoGuardado = saludoRepository.save(saludo);

        return SaludoResponse.builder()
                .id(saludoGuardado.getId())
                .mensaje(saludoGuardado.getMensaje())
                .build();
    }
}
