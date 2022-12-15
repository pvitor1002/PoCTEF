package br.com.puc.TEF.application.service.handle;

import br.com.puc.TEF.domain.entities.maquinaestado.FaseEvento;
import br.com.puc.TEF.domain.types.TipoEventoTransferencia;
import br.com.puc.statemachine.EventContext;
import br.com.puc.statemachine.TransitionAction;

public interface TransferenciaSenhaProcessadaHandle extends TransitionAction<TipoEventoTransferencia, EventContext<TipoEventoTransferencia>, FaseEvento> {
}
