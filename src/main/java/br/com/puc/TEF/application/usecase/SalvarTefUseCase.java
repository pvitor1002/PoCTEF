package br.com.puc.TEF.application.usecase;

import br.com.puc.TEF.domain.entities.TEF;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.UUID;

public interface SalvarTefUseCase {

    TEF buscar(UUID transactionId);
    void salvar(TEF tef) throws JsonProcessingException;
}
