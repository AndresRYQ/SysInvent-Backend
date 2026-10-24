package com.agrihusac.SysInvent.service.correo;

public interface CorreoService {

    void enviarCorreoHtml(String destinatario, String asunto, String contenidoHtml);
}
