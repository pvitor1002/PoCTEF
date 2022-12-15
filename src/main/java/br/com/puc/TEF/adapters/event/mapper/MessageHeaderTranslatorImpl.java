package br.com.puc.TEF.adapters.event.mapper;

import br.com.puc.TEF.adapters.datastore.ContextDatastore;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MessageHeaderTranslatorImpl implements MessageHeaderTranslator{

    private final ContextDatastore contextDatastore;

    @Override
    public void translate(MessageHeaders messageHeaders){

        final Map<String, Object> headers = new HashMap<>();

        messageHeaders.forEach((k, v) -> {
            try {
                headers.put(k, new String((byte[]) v, StandardCharsets.UTF_8));
            }catch(Exception e){
                System.err.println("Objeto inválido");
            }
        });
        contextDatastore.setHeaders(headers);
        contextDatastore.setTransactionId(UUID.fromString(contextDatastore.getHeaders().get("transaction_id").toString()));
    }

    @Override
    public void translateString(MessageHeaders messageHeaders){

        final Map<String, Object> headers = new HashMap<>();

        messageHeaders.forEach((k, v) -> {
            try {
                headers.put(k, v);
            }catch(Exception e){
                System.err.println("Objeto inválido");
            }
        });
        contextDatastore.setHeaders(headers);
    }
}
