package br.com.puc.TEF.adapters.beans;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.CqlSessionFactoryBean;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.aws.mcs.auth.SigV4AuthProvider;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.security.*;
import java.security.cert.CertificateException;
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

        try (SigV4AuthProvider provider = new SigV4AuthProvider("sa-east-1")) {
            CqlSessionFactoryBean session = new CqlSessionFactoryBean();
            List<InetSocketAddress> contactPoints = Collections.singletonList(new InetSocketAddress("cassandra.sa-east-1.amazonaws.com", 9142));
            session.setSessionBuilderConfigurer(config -> {
                try {
                    return config.addContactPoints(contactPoints)
                            .withKeyspace("keyspace_transferencias")
                            .withAuthProvider(provider)
                            .withConfigLoader(
                                    DriverConfigLoader.programmaticBuilder()
                                            .withDuration(DefaultDriverOption.REQUEST_TIMEOUT, Duration.ofMillis(20000))
                                            .withDuration(DefaultDriverOption.CONNECTION_CONNECT_TIMEOUT, Duration.ofMillis(20000))
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
                            .withSslContext(this.sslContext("cassandra_truststore.jks", "amazon"))
                            .withLocalDatacenter("sa-east-1");
                } catch (Exception e) {
                    throw new RuntimeException();
                }
            });

            session.setKeyspaceName("keyspace_transferencias");
            return session;
        }
    }

    private SSLContext sslContext(String keystoreFile, String password) throws KeyStoreException, IOException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException, CertificateException {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        try (InputStream in = new FileInputStream(keystoreFile)) {
            keyStore.load(in, password.toCharArray());
        }

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password.toCharArray());

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());

        return sslContext;
    }
}
