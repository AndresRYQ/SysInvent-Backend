package com.agrihusac.SysInvent.event.listener;

import com.agrihusac.SysInvent.event.UsuarioRegistradoEvent;
import com.agrihusac.SysInvent.service.correo.CorreoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.util.HtmlUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class UsuarioRegistradoCorreoListener {

    private final CorreoService correoService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void enviarCorreoBienvenida(UsuarioRegistradoEvent event) {
        String nombres = HtmlUtils.htmlEscape(event.nombres().trim());
        String usuario = HtmlUtils.htmlEscape(event.usuario().trim());
        String contrasenaTemporal = HtmlUtils.htmlEscape(event.contrasenaTemporal());

        String contenidoHtml = """
                <p>Hola, %s:</p>
                <p>Te damos la bienvenida a <strong>SysInvent</strong>.</p>
                <p>Tu usuario es: <strong>%s</strong></p>
                <p>Tu contraseña inicial es: <strong>%s</strong></p>
                <p>Por seguridad, cámbiala cuando el sistema habilite esa opción.</p>
                """.formatted(nombres, usuario, contrasenaTemporal);

        try {
            correoService.enviarCorreoHtml(
                    event.destinatario(),
                    "Bienvenido a SysInvent",
                    contenidoHtml);
        } catch (RuntimeException exception) {
            log.warn("No fue posible enviar el correo de bienvenida a {}", event.destinatario(), exception);
        }
    }
}
