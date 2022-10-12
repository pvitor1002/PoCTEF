package br.com.puc.TEF.adapters.event.producer;

import br.com.PoC.ComandoCredito;
import br.com.PoC.ComandoDebito;
import br.com.PoC.ComandoSenha;
import br.com.puc.TEF.adapters.datastore.ContextDatastore;
import br.com.puc.TEF.adapters.event.response.TransferenciaConcluida;
import br.com.puc.TEF.domain.entities.TEF;
import br.com.puc.TEF.domain.entities.maquinaestado.EventoMudancaEstado;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TransferenciaProducer {

    private final KafkaTemplate<String, GenericRecord> kafkaTemplate;
    private final KafkaTemplate<String, String> kafkaTemplateString;
    private final KafkaTemplate<String, EventoMudancaEstado> kafkaTemplateEstado;
    private final ContextDatastore contextDatastore;

    public void produce() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> headers = new HashMap<>();
        headers.put("replyChannel", contextDatastore.getHeaders().get("replyChannel").toString());
        headers.put("errorChannel", contextDatastore.getHeaders().get("errorChannel").toString());
        headers.put("instanceId", contextDatastore.getHeaders().get("instanceId").toString());
        Message<String> message = MessageBuilder.withPayload(
                mapper.writeValueAsString(TransferenciaConcluida.builder()
                        .cpf("12249932689")
                        .nome("Paulo Vitor")
                        .build())
        ).copyHeaders(new MessageHeaders(headers)).build();

        kafkaTemplateString.send(message).addCallback(
                successCallback -> {
                    System.out.println("Mensagem enviada com sucesso!");
                },
                failureCallback -> {
                    System.err.println("Erro ao enviar mensagem!");
                });
    }

    public void produceComandoSenha(TEF tef){
        ProducerRecord<String, GenericRecord> record = new ProducerRecord<>("comando-senha", UUID.randomUUID().toString(),
                ComandoSenha.newBuilder()
                        .setSenha(tef.getAutenticacao())
                        .build()
                );
        record.headers().add("transaction_id", tef.getTransactionId().toString().getBytes(StandardCharsets.UTF_8));
        record.headers().add("replyChannel", contextDatastore.getHeaders().get("replyChannel").toString().getBytes());
        record.headers().add("errorChannel", contextDatastore.getHeaders().get("errorChannel").toString().getBytes());
        record.headers().add("instanceId", contextDatastore.getHeaders().get("instanceId").toString().getBytes());

        kafkaTemplate.send(record);
    }

    public void produceComandoDebito(TEF tef){
        ProducerRecord<String, GenericRecord> record = new ProducerRecord<>("comando-debito", UUID.randomUUID().toString(),
                ComandoDebito.newBuilder()
                        .setAgencia(tef.getContaOrigem().getAgencia())
                        .setConta(tef.getContaOrigem().getConta())
                        .setDac(tef.getContaOrigem().getDac())
                        .setValor(tef.getValor().toString())
                        .build()
        );

        record.headers().add("transaction_id", tef.getTransactionId().toString().getBytes(StandardCharsets.UTF_8));
        record.headers().add("replyChannel", contextDatastore.getHeaders().get("replyChannel").toString().getBytes());
        record.headers().add("errorChannel", contextDatastore.getHeaders().get("errorChannel").toString().getBytes());
        record.headers().add("instanceId", contextDatastore.getHeaders().get("instanceId").toString().getBytes());

        kafkaTemplate.send(record);
    }

    public void produceComandoCredito(TEF tef){
        ProducerRecord<String, GenericRecord> record = new ProducerRecord<>("comando-credito", UUID.randomUUID().toString(),
                ComandoCredito.newBuilder()
                        .setAgencia(tef.getContaDestino().getAgencia())
                        .setConta(tef.getContaDestino().getConta())
                        .setDac(tef.getContaDestino().getDac())
                        .setValor(tef.getValor().toString())
                        .build()
        );

        record.headers().add("transaction_id", tef.getTransactionId().toString().getBytes(StandardCharsets.UTF_8));
        record.headers().add("replyChannel", contextDatastore.getHeaders().get("replyChannel").toString().getBytes());
        record.headers().add("errorChannel", contextDatastore.getHeaders().get("errorChannel").toString().getBytes());
        record.headers().add("instanceId", contextDatastore.getHeaders().get("instanceId").toString().getBytes());
        kafkaTemplate.send(record);
    }

    public void produceInternalTopic(EventoMudancaEstado evento) {
        final ProducerRecord<String, EventoMudancaEstado> producerRecord = new ProducerRecord<>("comando-estado", UUID.randomUUID().toString(), evento);

        producerRecord.headers().add("replyChannel", contextDatastore.getHeaders().get("replyChannel").toString().getBytes());
        producerRecord.headers().add("errorChannel", contextDatastore.getHeaders().get("errorChannel").toString().getBytes());
        producerRecord.headers().add("instanceId", contextDatastore.getHeaders().get("instanceId").toString().getBytes());

        kafkaTemplateEstado.send(producerRecord).addCallback(s -> {
                    System.out.println("Enviado para tópico interno.");
                },
                f -> {
                    System.err.println("Erro ao enviar mensagem.");
                });
    }
}
