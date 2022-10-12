package br.com.puc.TEF.adapters.gateway.filters;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.messaging.Message;

@MessageEndpoint
public class MessageFilter {

    public boolean accept(Message<?> payload) {
        System.err.println("Passando pelo filtro de descarte: " + IntegrationFlowDefinitions.instanceId);
        return payload.getHeaders().get("instanceId").toString().equals(IntegrationFlowDefinitions.instanceId);
    }
}
