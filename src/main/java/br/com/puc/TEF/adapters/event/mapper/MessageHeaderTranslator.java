package br.com.puc.TEF.adapters.event.mapper;

import org.springframework.messaging.MessageHeaders;

public interface MessageHeaderTranslator {

    void translate(MessageHeaders headers);
    void translateString(MessageHeaders headers);
}
