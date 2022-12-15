package br.com.puc.TEF.adapters.controller.server.contract.request;

import br.com.puc.TEF.adapters.controller.server.contract.Conta;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferenciaRequest {

    @Valid
    @NotNull(message = "conta_origem nao deve ser nulo")
    @JsonProperty("conta_origem")
    private Conta contaOrigem;

    @Valid
    @NotNull(message = "conta_destino nao deve ser nulo")
    @JsonProperty("conta_destino")
    private Conta contaDestino;

    @NotNull(message = "autenticacao nao deve ser nulo")
    @NotBlank(message = "autenticacao nao deve ser vazio")
    @JsonProperty("autenticacao")
    private String autenticacao;

    @NotNull(message = "valor nao deve ser nulo")
    @Digits(fraction = 2, integer = 15, message= "valor precisa ter no maximo 15 inteiros e 2 casas decimais. Ex.: 10.55")
    @DecimalMin(value = "0.01", message = "valor tem que ser maior que 1 centavo.")
    @JsonProperty("valor")
    private BigDecimal valor;
}
