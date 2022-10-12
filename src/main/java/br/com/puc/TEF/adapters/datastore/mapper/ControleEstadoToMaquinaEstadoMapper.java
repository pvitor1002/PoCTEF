package br.com.puc.TEF.adapters.datastore.mapper;

import br.com.puc.TEF.adapters.datastore.entity.ControleEstadoEntity;
import br.com.puc.TEF.adapters.datastore.entity.ControleEstadoEntityBody;
import br.com.puc.TEF.domain.entities.maquinaestado.MaquinaEstado;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@Component
public class ControleEstadoToMaquinaEstadoMapper implements DatabaseMapper<ControleEstadoEntity, MaquinaEstado> {

    @Override
    public MaquinaEstado mapFromDatabase(ControleEstadoEntity in){
        ObjectMapper mapper = new ObjectMapper();
        try{
            ControleEstadoEntityBody body = mapper.readValue(in.getStateControlBody(), ControleEstadoEntityBody.class);

            return MaquinaEstado.builder()
                    .context(body.getStateMachineContext())
                    .dataCriacao(OffsetDateTime.ofInstant(in.getDateTime().toInstant(), ZoneId.of("America/Sao_Paulo")))
                    .id(in.getCodigoCorrelacao())
                    .build();

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public ControleEstadoEntity mapToDatabase(MaquinaEstado in) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        try{
            return ControleEstadoEntity.builder()
                    .codigoCorrelacao(in.getId())
                    .dateTime(Date.from(in.getDataCriacao().toInstant()))
                    .eventType(in.getContext().getCurrentStatus())
                    .stateControlBody(mapper.writeValueAsString(ControleEstadoEntityBody.builder()
                            .stateMachineContext(in.getContext())
                            .build()))
                    .build();
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}
