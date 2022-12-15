package br.com.puc.TEF.application.service.handle;

import br.com.puc.TEF.domain.entities.maquinaestado.FaseEvento;
import br.com.puc.TEF.domain.types.TipoEventoTransferencia;
import br.com.puc.statemachine.EventContext;
import br.com.puc.statemachine.StateMachineContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TimeoutHandleImpl implements TimeoutHandle {
    @Override
    public void execute(EventContext<TipoEventoTransferencia> context, TipoEventoTransferencia tipoEventoTransferencia, StateMachineContext<FaseEvento, TipoEventoTransferencia> stateContext) throws JsonProcessingException {
        System.err.println("Transação finalizada por timeout.");
    }
}
