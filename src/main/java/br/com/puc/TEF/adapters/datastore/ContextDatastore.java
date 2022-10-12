package br.com.puc.TEF.adapters.datastore;

import java.util.Map;
import java.util.UUID;

public interface ContextDatastore {

    void setHeaders(Map<String, Object> headers);
    void setTransactionId(UUID transactionId);

    Map<String, Object> getHeaders();
    UUID getTransactionId();
}
