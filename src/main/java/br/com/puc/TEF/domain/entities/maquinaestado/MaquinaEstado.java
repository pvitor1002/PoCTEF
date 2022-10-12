package br.com.puc.TEF.domain.entities.maquinaestado;

import br.com.puc.TEF.domain.types.TipoEventoTransferencia;
import br.com.puc.statemachine.StateMachineContext;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
public class MaquinaEstado {
    private UUID id;
    private OffsetDateTime dataCriacao;
    private StateMachineContext<FaseEvento, TipoEventoTransferencia> context;
}
