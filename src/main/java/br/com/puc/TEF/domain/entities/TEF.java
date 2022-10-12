package br.com.puc.TEF.domain.entities;

import br.com.puc.TEF.adapters.controller.server.contract.Conta;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TEF {

    private UUID transactionId;
    private Conta contaOrigem;
    private Conta contaDestino;
    private String autenticacao;
    private BigDecimal valor;
}
