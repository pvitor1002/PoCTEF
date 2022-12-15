package br.com.puc.TEF.adapters.datastore.mapper;

import br.com.puc.TEF.adapters.controller.server.contract.Conta;
import br.com.puc.TEF.adapters.datastore.entity.TefEntity;
import br.com.puc.TEF.domain.entities.TEF;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Component;

@Component
public class TefEntityToTefMapper implements DatabaseMapper<TefEntity, TEF>{
    @Override
    public TEF mapFromDatabase(TefEntity in) {
        return TEF.builder()
                .transactionId(in.getTransactionId())
                .contaOrigem(Conta.builder()
                        .dac(in.getDacOrigem())
                        .conta(in.getContaOrigem())
                        .agencia(in.getAgenciaOrigem())
                        .build())
                .contaDestino(Conta.builder()
                        .agencia(in.getAgenciaDestino())
                        .dac(in.getDacDestino())
                        .conta(in.getContaDestino())
                        .build())
                .valor(in.getValor())
                .build();
    }

    @Override
    public TefEntity mapToDatabase(TEF in) throws JsonProcessingException {
        return TefEntity.builder()
                .transactionId(in.getTransactionId())
                .agenciaDestino(in.getContaDestino().getAgencia())
                .agenciaOrigem(in.getContaOrigem().getAgencia())
                .contaDestino(in.getContaDestino().getConta())
                .dacDestino(in.getContaDestino().getDac())
                .dacOrigem(in.getContaOrigem().getDac())
                .contaOrigem(in.getContaOrigem().getConta())
                .valor(in.getValor())
                .build();
    }
}
