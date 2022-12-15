package br.com.puc.TEF.adapters.controller.server.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Conta {

    @NotNull(message = "numero_agencia nao deve ser nulo")
    @NotBlank(message = "numero_agencia nao deve ser vazio")
    @JsonProperty("numero_agencia")
    private String agencia;

    @NotNull(message = "numero_conta nao deve ser nulo")
    @NotBlank(message = "numero_conta nao deve ser vazio")
    @JsonProperty("numero_conta")
    private String conta;

    @NotNull(message = "numero_digito_verificador nao deve ser nulo")
    @NotBlank(message = "numero_digito_verificador nao deve ser vazio")
    @JsonProperty("numero_digito_verificador")
    private String dac;
}
