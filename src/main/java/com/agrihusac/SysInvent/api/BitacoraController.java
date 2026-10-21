package com.agrihusac.SysInvent.api;

import com.agrihusac.SysInvent.model.request.BitacoraRequest;
import com.agrihusac.SysInvent.service.bitacora.BitacoraService;
import com.agrihusac.SysInvent.utils.MessageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bitacora")
@RequiredArgsConstructor
public class BitacoraController {

    private final BitacoraService bitacoraService;

    @PostMapping
    public ResponseEntity<MessageResponse> registrar(@Valid @RequestBody BitacoraRequest request) {
        return bitacoraService.registrar(request);
    }
}
