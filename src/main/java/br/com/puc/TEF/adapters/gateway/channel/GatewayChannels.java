package br.com.puc.TEF.adapters.gateway.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface GatewayChannels {
    String REQUEST = "request";
    String REPLY = "reply";
    String RESPONSE = "response";
    String LATERAL = "lateral";

    @Output(REQUEST)
    MessageChannel request();

    @Input(REPLY)
    SubscribableChannel reply();

    @Input(RESPONSE)
    MessageChannel response();

    @Input(LATERAL)
    MessageChannel lateral();
}
