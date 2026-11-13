package com.agrihusac.SysInvent;

import com.agrihusac.SysInvent.config.SecurityConfig;
import com.agrihusac.SysInvent.security.JwtAccessDeniedHandler;
import com.agrihusac.SysInvent.security.JwtAuthenticationEntryPoint;
import com.agrihusac.SysInvent.security.JwtAuthenticationFilter;
import com.agrihusac.SysInvent.service.JwtService;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CatalogoPruebaController.class)
@Import({
        SecurityConfig.class,
        JwtAuthenticationFilter.class,
        JwtAuthenticationEntryPoint.class,
        JwtAccessDeniedHandler.class
})
class CatalogosSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @ParameterizedTest
    @MethodSource("catalogosProtegidos")
    void permiteConsultarCatalogosAUsuarioAutenticado(
            String ruta,
            String authority
    ) throws Exception {
        mockMvc.perform(
                        get(ruta)
                                .with(user("usuario-prueba")
                                        .authorities(
                                                new SimpleGrantedAuthority(
                                                        "MODULE_/usuarios"
                                                )
                                        ))
                )
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("catalogosProtegidos")
    void permiteModificarCatalogoConModuloCorrespondiente(
            String ruta,
            String authority
    ) throws Exception {
        mockMvc.perform(
                        post(ruta)
                                .with(user("usuario-prueba")
                                        .authorities(
                                                new SimpleGrantedAuthority(
                                                        authority
                                                )
                                        ))
                )
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("catalogosProtegidos")
    void rechazaModificacionSinModuloCorrespondiente(
            String ruta,
            String authority
    ) throws Exception {
        mockMvc.perform(
                        post(ruta)
                                .with(user("usuario-prueba")
                                        .authorities(
                                                new SimpleGrantedAuthority(
                                                        "MODULE_/usuarios"
                                                )
                                        ))
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void rechazaConsultaSinAutenticacion() throws Exception {
        mockMvc.perform(
                        get("/api/v1/catalogos/categorias")
                )
                .andExpect(status().isUnauthorized());
    }

    private static Stream<Arguments> catalogosProtegidos() {
        return Stream.of(
                Arguments.of(
                        "/api/v1/catalogos/tipos-producto",
                        "MODULE_/tipos-producto"
                ),
                Arguments.of(
                        "/api/v1/catalogos/tipos-documento",
                        "MODULE_/tipos-documento"
                ),
                Arguments.of(
                        "/api/v1/catalogos/destinos",
                        "MODULE_/destinos"
                ),
                Arguments.of(
                        "/api/v1/catalogos/unidades-medida",
                        "MODULE_/unidades-medida"
                ),
                Arguments.of(
                        "/api/v1/catalogos/categorias",
                        "MODULE_/categorias"
                ),
                Arguments.of(
                        "/api/v1/catalogos/centros-costo",
                        "MODULE_/centros-costos"
                ),
                Arguments.of(
                        "/api/v1/catalogos/partes-equipo",
                        "MODULE_/partes-equipo"
                ),
                Arguments.of(
                        "/api/categorias",
                        "MODULE_/categorias"
                )
        );
    }
}

@RestController
class CatalogoPruebaController {

    @RequestMapping(
            value = {
                    "/api/v1/catalogos/{catalogo}",
                    "/api/categorias"
            },
            method = {
                    RequestMethod.GET,
                    RequestMethod.POST,
                    RequestMethod.PUT,
                    RequestMethod.DELETE
            }
    )
    void catalogo() {
        // Controlador mínimo utilizado únicamente para probar las reglas.
    }
}