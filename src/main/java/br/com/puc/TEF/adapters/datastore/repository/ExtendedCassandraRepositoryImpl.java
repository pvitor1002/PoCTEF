package br.com.puc.TEF.adapters.datastore.repository;

import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.InsertOptions;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.query.CassandraEntityInformation;
import org.springframework.data.cassandra.repository.support.SimpleCassandraRepository;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.Optional;

public class ExtendedCassandraRepositoryImpl<A, B> extends SimpleCassandraRepository<A, B> implements  ExtendedCassandraRepository<A, B>{

    private final CassandraEntityInformation<A, B> entityInformation;
    private final CassandraOperations operations;

    public ExtendedCassandraRepositoryImpl(CassandraEntityInformation<A, B> metadata, CassandraOperations operations) {
        super(metadata, operations);

        this.entityInformation = metadata;
        this.operations = operations;
    }

    @Query(idempotent = Query.Idempotency.IDEMPOTENT)
    @Override
    public Optional<A> findById(B id) {return super.findById(id);}

    @Query(idempotent = Query.Idempotency.IDEMPOTENT)
    @Override
    public <S extends A> S save(S entity) { return super.save(entity);}

    @Query(idempotent = Query.Idempotency.IDEMPOTENT)
    @Override
    public <S extends A> S insert(S entity) { return super.insert(entity); }

    @Query(idempotent = Query.Idempotency.IDEMPOTENT)
    @Override
    public void delete(A entity) { super.delete(entity); }

    @Query(idempotent = Query.Idempotency.IDEMPOTENT)
    @Override
    public void deleteById(B id) { super.deleteById(id); }

    @Query(idempotent = Query.Idempotency.IDEMPOTENT)
    @Override
    public <C extends A> C save(C entity, int ttl, TemporalUnit temporalUnit) {
        Duration ttlDuration = Duration.of(ttl, temporalUnit);
        InsertOptions insertOptions = InsertOptions.builder().timeout(ttlDuration).build();
        operations.insert(entity, insertOptions);
        return entity;
    }
}
