package com.agrihusac.SysInvent.repository;

import com.agrihusac.SysInvent.model.entity.SaludoEntity;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class SaludoRepository {

    private final AtomicLong sequence = new AtomicLong(0);
    private final Map<Long, SaludoEntity> saludos = new ConcurrentHashMap<>();

    public SaludoEntity save(SaludoEntity saludo) {
        if (saludo.getId() == null) {
            saludo.setId(sequence.incrementAndGet());
        }
        saludos.put(saludo.getId(), saludo);
        return saludo;
    }
}
