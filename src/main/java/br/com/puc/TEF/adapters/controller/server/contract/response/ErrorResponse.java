package br.com.puc.TEF.adapters.controller.server.contract.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    @JsonProperty("codigo")
    private String codigo;

    @JsonProperty("mensagem")
    private String mensagem;

}
