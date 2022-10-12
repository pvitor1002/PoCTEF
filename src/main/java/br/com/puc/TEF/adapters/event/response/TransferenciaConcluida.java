package br.com.puc.TEF.adapters.event.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferenciaConcluida {
    private String nome;
    private String cpf;
}
