package br.com.puc.TEF.adapters.datastore.repository;

import br.com.puc.TEF.adapters.datastore.entity.ControleEstadoEntity;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StateControlRepository extends CassandraRepository<ControleEstadoEntity, UUID> {
}
