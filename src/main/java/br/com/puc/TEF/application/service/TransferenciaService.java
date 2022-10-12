package br.com.puc.TEF.application.service;

import br.com.puc.TEF.domain.entities.TEF;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface TransferenciaService {

    TEF execute(TEF tef) throws JsonProcessingException;
}
