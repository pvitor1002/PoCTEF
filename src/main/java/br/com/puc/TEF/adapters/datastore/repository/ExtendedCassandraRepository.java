package br.com.puc.TEF.adapters.datastore.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.time.temporal.TemporalUnit;

@NoRepositoryBean
public interface ExtendedCassandraRepository<A, B> extends CassandraRepository<A, B> {

    <C extends A> C save(C entity, int ttl, TemporalUnit temporalUnit);
}
