package com.agrihusac.SysInvent.config;

import com.agrihusac.SysInvent.model.entity.RolEntity;
import com.agrihusac.SysInvent.model.entity.UsuarioEntity;
import com.agrihusac.SysInvent.model.entity.UsuarioRolEntity;
import com.agrihusac.SysInvent.repository.RolRepository;
import com.agrihusac.SysInvent.repository.UsuarioRepository;
import com.agrihusac.SysInvent.repository.UsuarioRolRepository;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataInitializer.class);
    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_ROLE = "ADMIN";

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioRolRepository usuarioRolRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${initial-admin.password:}")
    private String initialAdminPassword;

    @Override
    @Transactional
    public void run(String... args) {
        if (usuarioRepository.findByUsuarioAndActivoTrue(ADMIN_USER).isPresent()) {
            return;
        }

        if (initialAdminPassword == null || initialAdminPassword.isBlank()) {
            LOGGER.warn("Initial admin creation skipped: INITIAL_ADMIN_PASSWORD is not configured");
            return;
        }

        RolEntity rol = rolRepository.save(RolEntity.builder()
                .nombre(ADMIN_ROLE)
                .descripcion("Administrador del sistema")
                .activo(Boolean.TRUE)
                .build());

        UsuarioEntity usuario = usuarioRepository.save(UsuarioEntity.builder()
                .usuario(ADMIN_USER)
                .nombres("Administrador")
                .apePaterno("Sistema")
                .apeMaterno("Local")
                .dni("00000000")
                .email("admin@local.test")
                .contrasena(passwordEncoder.encode(initialAdminPassword))
                .activo(Boolean.TRUE)
                .build());

        usuarioRolRepository.save(UsuarioRolEntity.builder()
                .usuarioId(usuario.getUsuarioId())
                .rolId(rol.getRolId())
                .activo(Boolean.TRUE)
                .fechaAsignacion(LocalDate.now())
                .build());
    }
}
