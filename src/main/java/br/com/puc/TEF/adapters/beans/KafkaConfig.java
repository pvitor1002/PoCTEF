package br.com.puc.TEF.adapters.beans;

import br.com.puc.TEF.domain.entities.maquinaestado.EventoMudancaEstado;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.listener.concurrency}")
    private String concurrency;

    @Value("${transferencia.kafka.schedule.group-id}")
    private String groupIdSchedule;

    @Value("${transferencia.kafka.schedule.concurrency}")
    private Integer concurrencySchedule;

    @Value("${spring.kafka.bootstrap-server}")
    private String bootstrapAddress;

    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, GenericRecord>> transferenciaListenerContainerFactoryBean(
            ConsumerFactory<String, GenericRecord> consumerFactory){
        final Map<String, Object> configurations = ((DefaultKafkaConsumerFactory<String, GenericRecord>) consumerFactory)
                .getConfigurationProperties();
        final Map<String, Object> mapaConfiguration = new HashMap<>(configurations);

        DefaultKafkaConsumerFactory<String, GenericRecord> consumerFactoryEdit = new DefaultKafkaConsumerFactory<>(mapaConfiguration);

        ConcurrentKafkaListenerContainerFactory<String, GenericRecord> containerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        containerFactory.setConsumerFactory(consumerFactoryEdit);
        //containerFactory.setErrorHandler(errorHandler);
        containerFactory.setConcurrency(Integer.parseInt(concurrency));
        containerFactory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return containerFactory;
    }

    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, EventoMudancaEstado>> estadoListenerContainerFactoryBean(
            ConsumerFactory<String, EventoMudancaEstado> consumerFactory) {
        ObjectMapper objectMapper = new ObjectMapper();
        final Map<String, Object> configurations = consumerFactory.getConfigurationProperties();
        final Map<String, Object> mapaConfiguration = new HashMap<>(configurations);

        JsonDeserializer<EventoMudancaEstado> deserializer = new JsonDeserializer<>(EventoMudancaEstado.class, objectMapper);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages(EventoMudancaEstado.class.getPackage().getName());
        deserializer.setUseTypeMapperForKey(true);

        DefaultKafkaConsumerFactory<String, Object> consumerFactoryEdit = new DefaultKafkaConsumerFactory(
                mapaConfiguration, new StringDeserializer(), deserializer);

        ConcurrentKafkaListenerContainerFactory<String, EventoMudancaEstado> containerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        containerFactory.setConsumerFactory(consumerFactoryEdit);
        containerFactory.setConcurrency(Integer.parseInt(concurrency));
        containerFactory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return containerFactory;
    }

    @Bean
    ConsumerFactory<String, GenericRecord> consumerFactory(){
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "transferencia");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        props.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081/");
        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    ConsumerFactory<String, EventoMudancaEstado> consumerFactoryEstado(){
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "transferencia");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    KafkaTemplate<String, GenericRecord> getKafkaTemplate(ProducerFactory<String, GenericRecord> producerFactory){
        KafkaTemplate<String, GenericRecord> template = new KafkaTemplate<>(producerFactory);
        template.setDefaultTopic("transferencia-concluida");
        return template;
    }

    @Bean
    KafkaTemplate<String, String> getKafkaTemplateString(ProducerFactory<String, String> producerFactory){
        KafkaTemplate<String, String> template = new KafkaTemplate<>(producerFactory);
        template.setDefaultTopic("transferencia-concluida");
        return template;
    }

    @Bean
    KafkaTemplate<String, EventoMudancaEstado> getKafkaTemplateEstado(ProducerFactory<String, EventoMudancaEstado> producerFactory){
        final Map<String, Object> configurations = ((DefaultKafkaProducerFactory) producerFactory)
                .getConfigurationProperties();
        final Map<String, Object> mapaConfiguration = new HashMap<>(configurations);

        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(mapaConfiguration, new StringSerializer(), new JsonSerializer<>()));
    }

    @Bean
    ProducerFactory<String, GenericRecord> producerFactory(){
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        props.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081/");
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    ProducerFactory<String, String> producerFactoryString(){
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    ProducerFactory<String, EventoMudancaEstado> producerFactoryEstado(){
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

}
