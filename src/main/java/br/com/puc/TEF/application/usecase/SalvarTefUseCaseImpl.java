package br.com.puc.TEF.application.usecase;

import br.com.puc.TEF.adapters.datastore.entity.TefEntity;
import br.com.puc.TEF.adapters.datastore.mapper.DatabaseMapper;
import br.com.puc.TEF.adapters.datastore.repository.TefRepository;
import br.com.puc.TEF.domain.entities.TEF;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SalvarTefUseCaseImpl implements SalvarTefUseCase{

    private final TefRepository tefRepository;
    private final DatabaseMapper<TefEntity, TEF> tefMapper;

    @Override
    public TEF buscar(UUID transactionId) {
        try {
            return tefMapper.mapFromDatabase(tefRepository.findById(transactionId).orElseThrow(() -> new RuntimeException()));
        } catch (Exception e){
            System.err.println("Tef não encontrada");
            return new TEF();
        }
    }

    @Override
    public void salvar(TEF tef) throws JsonProcessingException {
        tefRepository.save(tefMapper.mapToDatabase(tef));
    }
}
