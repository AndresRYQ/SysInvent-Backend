package com.agrihusac.SysInvent;

import static org.assertj.core.api.Assertions.assertThat;

import com.agrihusac.SysInvent.model.request.ActualizarPerfilRequest;
import com.agrihusac.SysInvent.model.request.ActualizarRolRequest;
import com.agrihusac.SysInvent.model.request.ActualizarUsuarioRequest;
import com.agrihusac.SysInvent.model.request.CambiarContrasenaRequest;
import com.agrihusac.SysInvent.model.request.LoginRequest;
import com.agrihusac.SysInvent.model.request.RolRequest;
import com.agrihusac.SysInvent.model.request.UsuarioRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Verifica que los requests sin @Size previo rechacen textos que superan
 * la longitud de su columna y acepten el valor exacto del límite.
 */
class LongitudRequestValidationTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void iniciar() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void cerrar() {
        factory.close();
    }

    private static String texto(int longitud) {
        return "a".repeat(longitud);
    }

    /**
     * Email válido para @Email (parte local de 64 y etiquetas de dominio de
     * 63 como máximo) con la longitud total exacta pedida.
     */
    private static String email(int longitud) {
        StringBuilder sb = new StringBuilder("a".repeat(64)).append('@');
        int restante = longitud - sb.length() - ".pe".length();
        while (restante > 0) {
            int etiqueta = Math.min(63, restante);
            sb.append("b".repeat(etiqueta));
            restante -= etiqueta;
            if (restante > 0) {
                sb.append('.');
                restante--;
            }
        }
        return sb.append(".pe").toString();
    }

    private static Set<String> camposInvalidos(Object request) {
        Set<ConstraintViolation<Object>> violaciones = validator.validate(request);
        return violaciones.stream()
                .map(v -> v.getPropertyPath().toString())
                .collect(Collectors.toSet());
    }

    @Test
    void rolRechazaTextosMayoresA255() {
        RolRequest request = RolRequest.builder()
                .nombre(texto(256)).descripcion(texto(256)).modulos(List.of(1)).build();
        assertThat(camposInvalidos(request)).containsExactlyInAnyOrder("nombre", "descripcion");

        request.setNombre(texto(255));
        request.setDescripcion(texto(255));
        assertThat(camposInvalidos(request)).isEmpty();
    }

    @Test
    void actualizarRolRechazaTextosMayoresA255() {
        ActualizarRolRequest request = ActualizarRolRequest.builder()
                .rolId(1).nombre(texto(256)).descripcion(texto(256)).modulos(List.of(1)).build();
        assertThat(camposInvalidos(request)).containsExactlyInAnyOrder("nombre", "descripcion");
    }

    @Test
    void usuarioRechazaTextosMayoresA255() {
        UsuarioRequest request = UsuarioRequest.builder()
                .dni("12345678").nombres(texto(256)).apePaterno(texto(256))
                .apeMaterno(texto(256)).email(email(256)).rolId(1).build();
        assertThat(camposInvalidos(request))
                .containsExactlyInAnyOrder("nombres", "apePaterno", "apeMaterno", "email");

        request.setNombres(texto(255));
        request.setApePaterno(texto(255));
        request.setApeMaterno(texto(255));
        request.setEmail(email(255));
        assertThat(camposInvalidos(request)).isEmpty();
    }

    @Test
    void actualizarUsuarioRechazaTextosMayoresA255() {
        ActualizarUsuarioRequest request = ActualizarUsuarioRequest.builder()
                .usuarioId(1).nombres(texto(256)).apePaterno(texto(256))
                .apeMaterno(texto(256)).email(email(256)).rolId(1).build();
        assertThat(camposInvalidos(request))
                .containsExactlyInAnyOrder("nombres", "apePaterno", "apeMaterno", "email");
    }

    @Test
    void perfilRechazaTextosMayoresA255() {
        ActualizarPerfilRequest request = new ActualizarPerfilRequest();
        request.setNombres(texto(256));
        request.setApePaterno(texto(256));
        request.setApeMaterno(texto(256));
        request.setEmail(email(256));
        assertThat(camposInvalidos(request))
                .containsExactlyInAnyOrder("nombres", "apePaterno", "apeMaterno", "email");
    }

    @Test
    void nuevaContrasenaAdmiteComoMaximo72Caracteres() {
        CambiarContrasenaRequest request = new CambiarContrasenaRequest();
        request.setContrasenaActual(texto(10));
        request.setNuevaContrasena(texto(73));
        request.setConfirmarContrasena(texto(73));
        assertThat(camposInvalidos(request))
                .containsExactlyInAnyOrder("nuevaContrasena", "confirmarContrasena");

        request.setNuevaContrasena(texto(72));
        request.setConfirmarContrasena(texto(72));
        assertThat(camposInvalidos(request)).isEmpty();
    }

    @Test
    void loginRechazaCredencialesMayoresA255() {
        LoginRequest request = new LoginRequest();
        request.setUsuario(texto(256));
        request.setContrasena(texto(256));
        assertThat(camposInvalidos(request)).containsExactlyInAnyOrder("usuario", "contrasena");
    }
}
