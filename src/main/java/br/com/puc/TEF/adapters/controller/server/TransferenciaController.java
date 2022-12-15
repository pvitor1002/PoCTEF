package br.com.puc.TEF.adapters.controller.server;

import br.com.puc.TEF.adapters.controller.server.contract.request.TransferenciaRequest;
import br.com.puc.TEF.adapters.controller.server.mapper.TefMapper;
import br.com.puc.TEF.adapters.gateway.queue.QueueGateway;
import br.com.puc.TEF.application.service.TransferenciaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/transferencias/v1/tef")
@RequiredArgsConstructor
public class TransferenciaController {

    private final TefMapper tefMapper;
    private final TransferenciaService transferenciaService;
    private final QueueGateway gateway;
    @SneakyThrows
    @PostMapping
    public ResponseEntity<?> tef(@Valid @RequestBody TransferenciaRequest transferenciaRequest,
                                 @RequestHeader HttpHeaders headers) throws JsonProcessingException {

        return ResponseEntity.ok(transferenciaService.execute(tefMapper.map(transferenciaRequest)));
    }

    @PostMapping("/concluir")
    public ResponseEntity response(@RequestBody byte[] responseByte, @RequestHeader Map<String, String> headers){

            System.err.println("Entrando no comunicacao lateral");
            gateway.comunicacaoLateral(MessageBuilder.withPayload(responseByte).copyHeaders(headers).build());

            return ResponseEntity.ok().build();
        }
}
