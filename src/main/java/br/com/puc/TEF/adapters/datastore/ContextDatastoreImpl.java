package br.com.puc.TEF.adapters.datastore;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Component
public class ContextDatastoreImpl implements ContextDatastore{

    private static ThreadLocal<Map<String, Object>> kafkaHeaders = new ThreadLocal<>();
    private static ThreadLocal<UUID> transactionId = new ThreadLocal<>();
    @Override
    public void setHeaders(Map<String, Object> headers){
        kafkaHeaders.set(headers);
    }

    @Override
    public void setTransactionId(UUID transaction) {
        transactionId.set(transaction);
    }

    @Override
    public Map<String, Object> getHeaders(){
        if(Objects.isNull(kafkaHeaders.get()))
            return new HashMap<>();
        return kafkaHeaders.get();
    }

    @Override
    public UUID getTransactionId() {
        return transactionId.get();
    }
}
