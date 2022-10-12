package br.com.puc.TEF;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
//@EnableCassandraRepositories(repositoryBaseClass = ExtendedCassandraRepositoryImpl.class)
public class TefApplication {

	public static void main(String[] args) {
		SpringApplication.run(TefApplication.class, args);
	}

}
