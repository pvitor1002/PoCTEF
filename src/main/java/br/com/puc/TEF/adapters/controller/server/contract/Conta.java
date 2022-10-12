package br.com.puc.TEF.adapters.controller.server.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Conta {

    @JsonProperty("numero_agencia")
    private String agencia;

    @JsonProperty("numero_conta")
    private String conta;

    @JsonProperty("numero_digito_verificador")
    private String dac;
}
