package com.agrihusac.SysInvent.config;

import com.agrihusac.SysInvent.model.entity.RolEntity;
import com.agrihusac.SysInvent.model.entity.UsuarioEntity;
import com.agrihusac.SysInvent.model.entity.UsuarioRolEntity;
import com.agrihusac.SysInvent.repository.RolRepository;
import com.agrihusac.SysInvent.repository.UsuarioRepository;
import com.agrihusac.SysInvent.repository.UsuarioRolRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASSWORD = "admin123";
    private static final String ADMIN_ROLE = "ADMIN";

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioRolRepository usuarioRolRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (usuarioRepository.findByUsuarioAndActivoTrue(ADMIN_USER).isPresent()) {
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
                .contrasena(passwordEncoder.encode(ADMIN_PASSWORD))
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
