package com.agrihusac.SysInvent.service.correo.impl;

import com.agrihusac.SysInvent.service.correo.CorreoService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class BrevoCorreoServiceImpl implements CorreoService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final boolean habilitado;
    private final String apiUrl;
    private final String apiKey;
    private final String remitenteEmail;
    private final String remitenteNombre;

    public BrevoCorreoServiceImpl(
            @Value("${brevo.enabled:false}") boolean habilitado,
            @Value("${brevo.api-url}") String apiUrl,
            @Value("${brevo.api-key:}") String apiKey,
            @Value("${brevo.sender.email:}") String remitenteEmail,
            @Value("${brevo.sender.name:SysInvent}") String remitenteNombre) {
        this.habilitado = habilitado;
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
        this.remitenteEmail = remitenteEmail;
        this.remitenteNombre = remitenteNombre;
    }

    @Override
    public void enviarCorreoHtml(
            String destinatario,
            String asunto,
            String contenidoHtml) {
        if (!habilitado) {
            return;
        }

        validarConfiguracion(destinatario, asunto, contenidoHtml);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", apiKey);

        Map<String, Object> body = Map.of(
                "sender", Map.of(
                        "name", remitenteNombre,
                        "email", remitenteEmail),
                "to", List.of(Map.of("email", destinatario.trim())),
                "subject", asunto.trim(),
                "htmlContent", contenidoHtml);

        try {
            ResponseEntity<Void> response = restTemplate.postForEntity(
                    apiUrl,
                    new HttpEntity<>(body, headers),
                    Void.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new IllegalStateException("Brevo no pudo enviar el correo.");
            }
        } catch (RestClientException exception) {
            throw new IllegalStateException("No fue posible enviar el correo mediante Brevo.", exception);
        }
    }

    private void validarConfiguracion(
            String destinatario,
            String asunto,
            String contenidoHtml) {
        if (!StringUtils.hasText(apiKey) || !StringUtils.hasText(remitenteEmail)) {
            throw new IllegalStateException(
                    "La configuración de Brevo no está completa. Define BREVO_API_KEY y BREVO_SENDER_EMAIL.");
        }

        if (!StringUtils.hasText(destinatario)
                || !StringUtils.hasText(asunto)
                || !StringUtils.hasText(contenidoHtml)) {
            throw new IllegalArgumentException("El destinatario, asunto y contenido son obligatorios.");
        }
    }
}
