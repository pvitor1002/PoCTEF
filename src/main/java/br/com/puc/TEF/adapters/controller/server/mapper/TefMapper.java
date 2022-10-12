package br.com.puc.TEF.adapters.controller.server.mapper;

import br.com.puc.TEF.adapters.controller.server.contract.request.TransferenciaRequest;
import br.com.puc.TEF.domain.entities.TEF;

public interface TefMapper {

    TEF map(TransferenciaRequest transferenciaRequest);
}
