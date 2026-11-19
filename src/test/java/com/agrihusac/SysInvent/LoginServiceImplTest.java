package com.agrihusac.SysInvent;

import com.agrihusac.SysInvent.model.entity.RolEntity;
import com.agrihusac.SysInvent.model.entity.UsuarioEntity;
import com.agrihusac.SysInvent.model.entity.UsuarioRolEntity;
import com.agrihusac.SysInvent.model.request.LoginRequest;
import com.agrihusac.SysInvent.model.response.auth.LoginResponse;
import com.agrihusac.SysInvent.repository.ModuloRepository;
import com.agrihusac.SysInvent.repository.RolModuloRepository;
import com.agrihusac.SysInvent.repository.RolRepository;
import com.agrihusac.SysInvent.repository.UsuarioRepository;
import com.agrihusac.SysInvent.repository.UsuarioRolRepository;
import com.agrihusac.SysInvent.service.JwtService;
import com.agrihusac.SysInvent.service.impl.LoginServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioRolRepository usuarioRolRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private RolModuloRepository rolModuloRepository;

    @Mock
    private ModuloRepository moduloRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private LoginServiceImpl loginService;

    @Test
    void rechazaLoginCuandoElRolAsignadoEstaInactivo() {
        UsuarioEntity usuario = UsuarioEntity.builder()
                .usuarioId(7)
                .usuario("usuario-prueba")
                .contrasena("hash-seguro")
                .activo(true)
                .build();

        UsuarioRolEntity usuarioRol = UsuarioRolEntity.builder()
                .usuarioId(7)
                .rolId(2)
                .activo(true)
                .build();

        RolEntity rolInactivo = RolEntity.builder()
                .rolId(2)
                .nombre("ALMACEN")
                .activo(false)
                .build();

        when(usuarioRepository.findByUsuarioAndActivoTrue("usuario-prueba"))
                .thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("ClaveSegura123", "hash-seguro"))
                .thenReturn(true);
        when(usuarioRolRepository.findAllByUsuarioIdAndActivoTrue(7))
                .thenReturn(List.of(usuarioRol));
        when(rolRepository.findById(2))
                .thenReturn(Optional.of(rolInactivo));

        ResponseEntity<LoginResponse> respuesta = loginService.login(
                new LoginRequest("usuario-prueba", "ClaveSegura123")
        );

        assertEquals(HttpStatus.FORBIDDEN, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertEquals(false, respuesta.getBody().success());
        assertEquals("El usuario no tiene un rol activo", respuesta.getBody().message());
        assertNull(respuesta.getBody().data());

        verifyNoInteractions(jwtService, rolModuloRepository, moduloRepository);
    }
}