package br.com.puc.TEF.adapters.gateway.queue;

import br.com.puc.TEF.adapters.gateway.channel.GatewayChannels;
import br.com.puc.TEF.adapters.gateway.filters.IntegrationFlowDefinitions;
import br.com.puc.TEF.domain.entities.TEF;
import br.com.puc.TEF.domain.entities.maquinaestado.EventoMudancaEstado;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;

@MessagingGateway
public interface QueueGateway {

    @Gateway(requestChannel = IntegrationFlowDefinitions.HANDLER_FLOW, replyChannel = GatewayChannels.RESPONSE, replyTimeout = 8000)
    String handle(@Payload EventoMudancaEstado payload);

    @Gateway(requestChannel = GatewayChannels.RESPONSE)
    void comunicacaoLateral(@Payload Message<byte[]> payload);
}
