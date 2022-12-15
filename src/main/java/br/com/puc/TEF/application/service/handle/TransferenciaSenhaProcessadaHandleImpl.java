package br.com.puc.TEF.application.service.handle;

import br.com.puc.TEF.adapters.event.producer.TransferenciaProducer;
import br.com.puc.TEF.application.usecase.SalvarTefUseCase;
import br.com.puc.TEF.domain.entities.TEF;
import br.com.puc.TEF.domain.entities.maquinaestado.FaseEvento;
import br.com.puc.TEF.domain.types.TipoEventoTransferencia;
import br.com.puc.statemachine.EventContext;
import br.com.puc.statemachine.StateMachineContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransferenciaSenhaProcessadaHandleImpl implements TransferenciaSenhaProcessadaHandle {

    private final TransferenciaProducer transferenciaProducer;
    private final SalvarTefUseCase salvarTefUseCase;

    @Override
    public void execute(EventContext<TipoEventoTransferencia> context, TipoEventoTransferencia tipoEventoTransferencia, StateMachineContext<FaseEvento, TipoEventoTransferencia> stateContext) throws JsonProcessingException {

        TEF tef = salvarTefUseCase.buscar(context.getId());

        transferenciaProducer.produceComandoCredito(tef);

        System.out.println("Evento Crédito produzido.");
    }
}
