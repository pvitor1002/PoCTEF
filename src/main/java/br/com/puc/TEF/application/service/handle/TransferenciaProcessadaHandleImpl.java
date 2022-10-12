package br.com.puc.TEF.application.service.handle;

import br.com.puc.TEF.adapters.event.producer.TransferenciaProducer;
import br.com.puc.TEF.domain.entities.maquinaestado.FaseEvento;
import br.com.puc.TEF.domain.types.TipoEventoTransferencia;
import br.com.puc.statemachine.EventContext;
import br.com.puc.statemachine.StateMachineContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransferenciaProcessadaHandleImpl implements TransferenciaProcessadaHandle{

    private final TransferenciaProducer transferenciaProducer;

    @Override
    public void execute(EventContext<TipoEventoTransferencia> context, TipoEventoTransferencia tipoEventoTransferencia,
                        StateMachineContext<FaseEvento, TipoEventoTransferencia> stateMachineContext) throws JsonProcessingException {

            System.out.println("Transferência Finalizada");

            transferenciaProducer.produce();
    }
}
