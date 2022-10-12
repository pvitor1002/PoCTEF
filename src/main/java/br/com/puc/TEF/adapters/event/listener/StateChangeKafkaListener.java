package br.com.puc.TEF.adapters.event.listener;

import br.com.puc.TEF.adapters.datastore.ContextDatastore;
import br.com.puc.TEF.adapters.event.mapper.MessageHeaderTranslator;
import br.com.puc.TEF.application.usecase.AtualizarMaquinaUsecase;
import br.com.puc.TEF.domain.entities.maquinaestado.EventoMudancaEstado;
import br.com.puc.TEF.domain.types.TipoEventoTransferencia;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class StateChangeKafkaListener {

    private final AtualizarMaquinaUsecase atualizarMaquinaUsecase;
    private final MessageHeaderTranslator messageHeaderTranslator;

    @KafkaListener(topics = "${transferencia.kafka.consumer.topic-estado}", groupId = "estado", containerFactory = "estadoListenerContainerFactoryBean")
    public void onEvent(final ConsumerRecord<String, EventoMudancaEstado> record,
                        @Headers MessageHeaders messageHeaders, final Acknowledgment ack) throws JsonProcessingException {
        try {
            if(!record.value().getType().equals(TipoEventoTransferencia.CRIAR_MAQUINA_ESTADOS)){
                messageHeaderTranslator.translate(messageHeaders);
            } else {
                messageHeaderTranslator.translateString(messageHeaders);
            }
            atualizarMaquinaUsecase.execute(record.value());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ack.acknowledge();
        }

    }
}
