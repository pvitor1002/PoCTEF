package br.com.puc.TEF.adapters.datastore;

import br.com.puc.TEF.domain.entities.maquinaestado.MaquinaEstado;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Optional;
import java.util.UUID;

public interface MaquinaEstadoDatastore {
    Optional<MaquinaEstado> get(UUID id) throws JsonProcessingException;
    boolean get(UUID id, String faseEvento);
    void put(MaquinaEstado maquinaEstado) throws JsonProcessingException;
}
