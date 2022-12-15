package br.com.puc.TEF.application.service;

import br.com.puc.TEF.adapters.datastore.ContextDatastore;
import br.com.puc.TEF.adapters.event.response.TransferenciaConcluida;
import br.com.puc.TEF.adapters.gateway.channel.GatewayChannels;
import br.com.puc.TEF.adapters.gateway.queue.QueueGateway;
import br.com.puc.TEF.application.usecase.SalvarTefUseCase;
import br.com.puc.TEF.domain.entities.TEF;
import br.com.puc.TEF.domain.entities.maquinaestado.EventoMudancaEstado;
import br.com.puc.TEF.domain.exceptions.TimeoutException;
import br.com.puc.TEF.domain.types.TipoEventoTransferencia;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@EnableBinding({GatewayChannels.class})
@EnableIntegration
@IntegrationComponentScan
@RequiredArgsConstructor
public class TransferenciaServiceImpl implements TransferenciaService {

    private final QueueGateway gateway;
    private final ObjectMapper mapper;
    private final ContextDatastore contextDatastore;
    private final SalvarTefUseCase salvarTefUseCase;

    @Override
    public TEF execute(TEF tef) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        EventoMudancaEstado<Object> eventoMudancaEstado = EventoMudancaEstado.builder()
                .eventoOriginal(mapper.writeValueAsString(tef))
                .tipoEventoTransferencia(TipoEventoTransferencia.CRIAR_MAQUINA_ESTADOS)
                .id(tef.getTransactionId())
                .build();

        contextDatastore.setTransactionId(tef.getTransactionId());
        salvarTefUseCase.salvar(tef);

        String response = Optional.ofNullable(gateway.handle(eventoMudancaEstado)).orElseThrow(() -> new TimeoutException("Timeout atingido."));

        mapper.readValue(response, TransferenciaConcluida.class);

        return tef;
    }
}
