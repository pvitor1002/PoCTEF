package br.com.puc.TEF.domain.entities.maquinaestado;

import br.com.puc.TEF.domain.types.TipoEventoTransferencia;
import br.com.puc.statemachine.EventContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventoMudancaEstado<TEvent> implements EventContext<TipoEventoTransferencia> {
    private UUID id;
    private TipoEventoTransferencia tipoEventoTransferencia;
    private TEvent eventoOriginal;
    private TipoEventoTransferencia type;

    public EventoMudancaEstado(String payload) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String estado64 = payload.replaceAll("\"", "");
        byte[] decoded = Base64.getDecoder().decode(estado64);
        EventoMudancaEstado mudancaEstado = mapper.readValue(decoded, EventoMudancaEstado.class);
        id = mudancaEstado.getId();
        tipoEventoTransferencia = mudancaEstado.getTipoEventoTransferencia();
        eventoOriginal = (TEvent) mudancaEstado.getEventoOriginal();
        type = mudancaEstado.getType();
    }

    @Override
    public TipoEventoTransferencia getType() {
        return this.tipoEventoTransferencia;
    }
}
