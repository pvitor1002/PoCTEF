package br.com.puc.TEF.adapters.event.listener;

import br.com.PoC.*;
import br.com.puc.TEF.adapters.datastore.ContextDatastore;
import br.com.puc.TEF.adapters.event.mapper.MessageHeaderTranslator;
import br.com.puc.TEF.adapters.event.producer.TransferenciaProducer;
import br.com.puc.TEF.domain.entities.maquinaestado.EventoMudancaEstado;
import br.com.puc.TEF.domain.types.TipoEventoTransferencia;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@KafkaListener(topics = "#{'${transferencia.kafka.consumer.topics}'.split(',')}", containerFactory = "transferenciaListenerContainerFactoryBean")
public class TransferenciaAtualizadaListener {

    private final TransferenciaProducer transferenciaProducer;
    private final MessageHeaderTranslator messageHeaderTranslator;
    private final ContextDatastore contextDatastore;

    @KafkaHandler
    public void transferenciaAtualizada(TefProcessada tefProcessada, @Headers MessageHeaders messageHeaders, Acknowledgment ack) throws IOException, InterruptedException {

        Thread.sleep(500);

        messageHeaderTranslator.translate(messageHeaders);

        System.out.println(tefProcessada.getMensagem());

        EventoMudancaEstado<Object> eventoMudancaEstado = buildEventoMudancaEstado(UUID.fromString(String.valueOf(contextDatastore.getHeaders().get("transaction_id"))), messageHeaders, tefProcessada.getType());

        transferenciaProducer.produceInternalTopic(eventoMudancaEstado);
        ack.acknowledge();
    }

    private EventoMudancaEstado<Object> buildEventoMudancaEstado(UUID transactionId, MessageHeaders headers, String type){
        return EventoMudancaEstado.builder()
                .eventoOriginal("")
                .tipoEventoTransferencia(TipoEventoTransferencia.fromString(type))
                .id(transactionId)
                .build();
    }
}
