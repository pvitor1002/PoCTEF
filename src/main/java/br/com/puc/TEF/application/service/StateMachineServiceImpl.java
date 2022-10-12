package br.com.puc.TEF.application.service;

import br.com.puc.TEF.adapters.datastore.MaquinaEstadoDatastore;
import br.com.puc.TEF.application.service.handle.TransferenciaProcessadaHandle;
import br.com.puc.TEF.domain.entities.maquinaestado.FaseEvento;
import br.com.puc.TEF.domain.entities.maquinaestado.MaquinaEstado;
import br.com.puc.TEF.domain.types.TipoEventoTransferencia;
import br.com.puc.statemachine.StateMachine;
import br.com.puc.statemachine.StateMachineBuilder;
import br.com.puc.statemachine.StateMachineContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class StateMachineServiceImpl implements StateMachineService{

    private final MaquinaEstadoDatastore estadoDatastore;
    private final TransferenciaProcessadaHandle transferenciaProcessadaHandle;

    @Override
    public StateMachine<FaseEvento, TipoEventoTransferencia> createStateMachine(UUID id) throws JsonProcessingException {
        MaquinaEstado maquinaEstado = MaquinaEstado.builder()
                .id(id)
                .dataCriacao(OffsetDateTime.now())
                .context(new StateMachineContext<>())
                .build();

        StateMachine<FaseEvento, TipoEventoTransferencia> stateMachine = getStateMachineBuilder()
                .build(maquinaEstado.getContext());

        this.estadoDatastore.put(maquinaEstado);

        return stateMachine;
    }

    @Override
    public StateMachine<FaseEvento, TipoEventoTransferencia> getStateMachine(UUID id) throws JsonProcessingException {
        MaquinaEstado maquinaEstado = estadoDatastore.get(id).orElseThrow(() -> {
            return new RuntimeException();
        });
        return getStateMachineBuilder().build(maquinaEstado.getContext());
    }

    @Override
    public void salvarStateMachine(UUID id, StateMachine<FaseEvento, TipoEventoTransferencia> stateMachine) throws JsonProcessingException {
        MaquinaEstado maquinaEstado = estadoDatastore.get(id).orElseThrow(() -> {
            return new RuntimeException();
        });

        maquinaEstado.setContext(stateMachine.getContext());

        estadoDatastore.put(maquinaEstado);
    }

    private StateMachineBuilder<FaseEvento,TipoEventoTransferencia> getStateMachineBuilder() {
        return StateMachineBuilder.create(FaseEvento.class, TipoEventoTransferencia.class)

                .initial(FaseEvento.TRANSFERENCIA_SOLICITADA)

                .addTransition(FaseEvento.TRANSFERENCIA_SOLICITADA, FaseEvento.TRANSFERENCIA_REALIZADA)
                .withEvent(TipoEventoTransferencia.SENHA_PROCESSADA)
                .withEvent(TipoEventoTransferencia.CREDITO_PROCESSADO)
                .withEvent(TipoEventoTransferencia.DEBITO_PROCESSADO)
                .withAction(transferenciaProcessadaHandle);

    }
}
