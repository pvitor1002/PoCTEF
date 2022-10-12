package br.com.puc.TEF.application.usecase;

import br.com.puc.TEF.domain.entities.maquinaestado.EventoMudancaEstado;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

public interface AtualizarMaquinaUsecase {
    void execute(EventoMudancaEstado eventoMudancaEstado) throws IOException;
}
