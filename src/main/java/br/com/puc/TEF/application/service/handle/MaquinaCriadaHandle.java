package br.com.puc.TEF.application.service.handle;

import br.com.puc.TEF.domain.entities.TEF;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface MaquinaCriadaHandle {
    void transferir(TEF tef) throws JsonProcessingException;
}
