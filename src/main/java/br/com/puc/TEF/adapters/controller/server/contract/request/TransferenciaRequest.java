package br.com.puc.TEF.adapters.controller.server.contract.request;

import br.com.puc.TEF.adapters.controller.server.contract.Conta;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferenciaRequest {

    @JsonProperty("conta_origem")
    private Conta contaOrigem;

    @JsonProperty("conta_destino")
    private Conta contaDestino;

    @JsonProperty("autenticacao")
    private String autenticacao;

    @JsonProperty("valor")
    private BigDecimal valor;
}
