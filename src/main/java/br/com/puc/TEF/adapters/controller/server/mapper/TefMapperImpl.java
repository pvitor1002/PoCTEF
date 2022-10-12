package br.com.puc.TEF.adapters.controller.server.mapper;

import br.com.puc.TEF.adapters.controller.server.contract.Conta;
import br.com.puc.TEF.adapters.controller.server.contract.request.TransferenciaRequest;
import br.com.puc.TEF.domain.entities.TEF;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TefMapperImpl implements TefMapper {
    @Override
    public TEF map(TransferenciaRequest transferenciaRequest) {
        return TEF.builder()
                .transactionId(UUID.randomUUID())
                .autenticacao(transferenciaRequest.getAutenticacao())
                .contaDestino(Conta.builder()
                        .agencia(transferenciaRequest.getContaDestino().getAgencia())
                        .conta(transferenciaRequest.getContaDestino().getConta())
                        .dac(transferenciaRequest.getContaDestino().getDac())
                        .build())
                .contaOrigem(Conta.builder()
                        .agencia(transferenciaRequest.getContaOrigem().getAgencia())
                        .conta(transferenciaRequest.getContaOrigem().getConta())
                        .dac(transferenciaRequest.getContaOrigem().getDac())
                        .build())
                .valor(transferenciaRequest.getValor())
                .build();
    }
}
