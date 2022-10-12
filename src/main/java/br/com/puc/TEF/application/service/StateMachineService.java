package br.com.puc.TEF.application.service;

import br.com.puc.TEF.domain.entities.maquinaestado.FaseEvento;
import br.com.puc.TEF.domain.types.TipoEventoTransferencia;
import br.com.puc.statemachine.StateMachine;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.UUID;

public interface StateMachineService {

    StateMachine<FaseEvento, TipoEventoTransferencia> createStateMachine(UUID id) throws JsonProcessingException;

    StateMachine<FaseEvento, TipoEventoTransferencia> getStateMachine(UUID id) throws JsonProcessingException;

    void salvarStateMachine(UUID id, StateMachine<FaseEvento, TipoEventoTransferencia> stateMachine) throws JsonProcessingException;
}
