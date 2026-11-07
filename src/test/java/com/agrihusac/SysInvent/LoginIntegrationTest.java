package com.agrihusac.SysInvent;

import com.agrihusac.SysInvent.model.entity.RolEntity;
import com.agrihusac.SysInvent.model.entity.UsuarioEntity;
import com.agrihusac.SysInvent.model.entity.UsuarioRolEntity;
import com.agrihusac.SysInvent.repository.RolRepository;
import com.agrihusac.SysInvent.repository.UsuarioRepository;
import com.agrihusac.SysInvent.repository.UsuarioRolRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LoginIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private UsuarioRolRepository usuarioRolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void limpiarDatos() {
        usuarioRolRepository.deleteAll();
        usuarioRepository.deleteAll();
        rolRepository.deleteAll();
    }

    @Test
    void loginConCredencialesValidasDevuelveToken() throws Exception {
        RolEntity rol = rolRepository.save(RolEntity.builder()
                .nombre("ADMIN")
                .descripcion("Administrador")
                .activo(Boolean.TRUE)
                .build());

        UsuarioEntity usuario = usuarioRepository.save(UsuarioEntity.builder()
                .usuario("admin-login")
                .nombres("Admin")
                .apePaterno("Login")
                .apeMaterno("Test")
                .dni("11111111")
                .email("admin-login@test.com")
                .contrasena(passwordEncoder.encode("Secret123!"))
                .activo(Boolean.TRUE)
                .build());

        usuarioRolRepository.save(UsuarioRolEntity.builder()
                .usuarioId(usuario.getUsuarioId())
                .rolId(rol.getRolId())
                .activo(Boolean.TRUE)
                .fechaAsignacion(LocalDate.now())
                .build());

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usuario\":\"admin-login\",\"contrasena\":\"Secret123!\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Inicio de sesion correcto"))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    void loginConCredencialesInvalidasDevuelveUnauthorized() throws Exception {
        RolEntity rol = rolRepository.save(RolEntity.builder()
                .nombre("ADMIN")
                .descripcion("Administrador")
                .activo(Boolean.TRUE)
                .build());

        UsuarioEntity usuario = usuarioRepository.save(UsuarioEntity.builder()
                .usuario("admin-login")
                .nombres("Admin")
                .apePaterno("Login")
                .apeMaterno("Test")
                .dni("22222222")
                .email("admin-login2@test.com")
                .contrasena(passwordEncoder.encode("Secret123!"))
                .activo(Boolean.TRUE)
                .build());

        usuarioRolRepository.save(UsuarioRolEntity.builder()
                .usuarioId(usuario.getUsuarioId())
                .rolId(rol.getRolId())
                .activo(Boolean.TRUE)
                .fechaAsignacion(LocalDate.now())
                .build());

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usuario\":\"admin-login\",\"contrasena\":\"incorrecta\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false));
    }
}
