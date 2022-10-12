package br.com.puc.TEF.adapters.beans;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.CqlSessionFactoryBean;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class CassandraConfig {

    @Value("${spring.data.cassandra.contact-points}")
    private String contactPoint;

    @Value("${spring.data.cassandra.port}")
    private int cassandraPort;

    @Value("${spring.data.cassandra.keyspace-name}")
    private String keyspaceName;

    @Bean
    public CqlSessionFactoryBean session(){
        CqlSessionFactoryBean session = new CqlSessionFactoryBean();
        List<InetSocketAddress> contactPoints = Collections.singletonList(new InetSocketAddress("localhost", 9042));
        session.setSessionBuilderConfigurer(config ->{
            try{
                return config.addContactPoints(contactPoints)
                        .withAuthCredentials("cassandra", "cassandra")
                             .withKeyspace("keyspace_transferencias")
                             .withConfigLoader(
                                     DriverConfigLoader.programmaticBuilder()
                                            .withDuration(DefaultDriverOption.REQUEST_TIMEOUT, Duration.ofMillis(12000))
                                            .withDuration(DefaultDriverOption.CONNECTION_CONNECT_TIMEOUT, Duration.ofMillis(10000))
                                            .withString(DefaultDriverOption.REQUEST_CONSISTENCY, "LOCAL_QUORUM")
                                            .withLong(DefaultDriverOption.CONNECTION_MAX_REQUESTS, 1024)
                                            .withLong(DefaultDriverOption.CONNECTION_POOL_LOCAL_SIZE, 9)
                                            .withLong(DefaultDriverOption.CONNECTION_POOL_REMOTE_SIZE, 9)
                                            .withString(DefaultDriverOption.RECONNECTION_POLICY_CLASS, "ExponentialReconnectionPolicy")
                                            .withDuration(DefaultDriverOption.RECONNECTION_BASE_DELAY, Duration.ofMillis(30))
                                            .withDuration(DefaultDriverOption.RECONNECTION_MAX_DELAY, Duration.ofMillis(500))
                                            .withBoolean(DefaultDriverOption.SSL_HOSTNAME_VALIDATION, false)
                                     .build()
                             )
                        .withLocalDatacenter("datacenter1");
            } catch (Exception e){
                throw new RuntimeException();
            }
        });

        session.setKeyspaceName("keyspace_transferencias");
        return session;
    }
}
