package br.com.puc.TEF.application.service.handle;

import br.com.puc.TEF.adapters.event.producer.TransferenciaProducer;
import br.com.puc.TEF.domain.entities.TEF;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MaquinaCriadaHandleImpl implements MaquinaCriadaHandle{

    private final TransferenciaProducer transferenciaProducer;

    @Override
    public void transferir(TEF tef) throws JsonProcessingException {
        transferenciaProducer.produceComandoSenha(tef);
    }
}
