package br.com.puc.TEF.adapters.datastore.entity;

import br.com.puc.TEF.adapters.controller.server.contract.Conta;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(value = "tef")
public class TefEntity {

    @PrimaryKey(value = "transaction_id")
    private UUID transactionId;

    @Column(value = "numero_agencia_origem")
    private String agenciaOrigem;

    @Column(value = "numero_conta_origem")
    private String contaOrigem;

    @Column(value = "numero_digito_verificador_origem")
    private String dacOrigem;

    @Column(value = "numero_agencia_destino")
    private String agenciaDestino;

    @Column(value = "numero_conta_destino")
    private String contaDestino;

    @Column(value = "numero_digito_verificador_destino")
    private String dacDestino;

    @Column(value = "valor")
    private BigDecimal valor;
}
