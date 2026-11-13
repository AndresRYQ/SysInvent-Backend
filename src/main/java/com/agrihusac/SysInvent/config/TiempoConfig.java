package com.agrihusac.SysInvent.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

/**
 * Reloj de la aplicación. Permite calcular "hoy" y "mes actual" en la zona
 * horaria del negocio (por defecto America/Lima) y sustituirlo en pruebas.
 */
@Configuration
public class TiempoConfig {

    @Bean
    @ConditionalOnMissingBean
    public Clock clock(@Value("${app.zona-horaria:America/Lima}") String zonaHoraria) {
        return Clock.system(ZoneId.of(zonaHoraria));
    }
}
