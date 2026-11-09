package com.agrihusac.SysInvent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.agrihusac.SysInvent.utils.MessageResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class MessageResponseTest {

    @Test
    void construyeRespuestaExitosaSinDependenciasExternas() {
        MessageResponse response = new MessageResponse(true, "Correcto", HttpStatus.OK.value(), null);

        assertTrue(response.isSuccess());
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }
}
