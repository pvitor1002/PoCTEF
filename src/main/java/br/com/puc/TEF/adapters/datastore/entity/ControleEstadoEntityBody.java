package br.com.puc.TEF.adapters.datastore.entity;

import br.com.puc.TEF.domain.entities.maquinaestado.FaseEvento;
import br.com.puc.TEF.domain.types.TipoEventoTransferencia;
import br.com.puc.statemachine.StateMachineContext;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ControleEstadoEntityBody {
    private StateMachineContext<FaseEvento, TipoEventoTransferencia> stateMachineContext;
}
