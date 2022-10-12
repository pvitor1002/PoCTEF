package br.com.puc.TEF.application.usecase;

import br.com.puc.TEF.application.service.StateMachineService;
import br.com.puc.TEF.application.service.handle.MaquinaCriadaHandle;
import br.com.puc.TEF.domain.entities.TEF;
import br.com.puc.TEF.domain.entities.maquinaestado.EventoMudancaEstado;
import br.com.puc.TEF.domain.entities.maquinaestado.FaseEvento;
import br.com.puc.TEF.domain.types.TipoEventoTransferencia;
import br.com.puc.statemachine.StateMachine;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AtualizarMaquinaUsecaseImpl implements AtualizarMaquinaUsecase{

    private final StateMachineService stateMachineService;
    private final MaquinaCriadaHandle maquinaCriadaHandle;
    private final ObjectMapper objectMapper;

    @Override
    public void execute(EventoMudancaEstado eventoMudancaEstado) throws IOException {

        if(TipoEventoTransferencia.CRIAR_MAQUINA_ESTADOS.equals(eventoMudancaEstado.getType())){
            stateMachineService.createStateMachine(eventoMudancaEstado.getId());

            maquinaCriadaHandle.transferir(objectMapper.readValue(eventoMudancaEstado.getEventoOriginal().toString(), TEF.class));
        } else {
            StateMachine<FaseEvento, TipoEventoTransferencia> stateMachine = stateMachineService
                    .getStateMachine(eventoMudancaEstado.getId());
            if(stateMachine.sendEvent(eventoMudancaEstado)){
                stateMachineService.salvarStateMachine(eventoMudancaEstado.getId(), stateMachine);
            }
        }
    }
}
