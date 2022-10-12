package br.com.puc.TEF.adapters.controller.server;

import br.com.puc.TEF.adapters.controller.server.contract.request.TransferenciaRequest;
import br.com.puc.TEF.adapters.controller.server.mapper.TefMapper;
import br.com.puc.TEF.application.service.TransferenciaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transferencias/v1/tef")
@RequiredArgsConstructor
public class TransferenciaController {

    private final TefMapper tefMapper;
    private final TransferenciaService transferenciaService;

    @PostMapping
    public ResponseEntity<?> tef(@RequestBody TransferenciaRequest transferenciaRequest,
                                 @RequestHeader HttpHeaders headers) throws JsonProcessingException {

        return ResponseEntity.ok(transferenciaService.execute(tefMapper.map(transferenciaRequest)));
    }
}
