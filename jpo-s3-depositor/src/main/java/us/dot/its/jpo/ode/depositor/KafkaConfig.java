// package us.dot.its.jpo.ode.depositor;

// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.Properties;
// import java.util.Set;
// import java.util.concurrent.ExecutionException;

// import org.apache.kafka.clients.admin.Admin;
// import org.apache.kafka.clients.admin.ListTopicsOptions;
// import org.apache.kafka.clients.admin.ListTopicsResult;
// import org.apache.kafka.clients.admin.NewTopic;
// import org.apache.kafka.common.KafkaFuture;
// import org.apache.kafka.common.serialization.StringSerializer;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.context.properties.ConfigurationProperties;
// import org.springframework.context.annotation.Bean;
// import org.springframework.kafka.config.TopicBuilder;
// import org.springframework.kafka.core.DefaultKafkaProducerFactory;
// import org.springframework.kafka.core.KafkaAdmin;
// import org.springframework.kafka.core.KafkaAdmin.NewTopics;
// import org.springframework.kafka.core.KafkaTemplate;
// import org.springframework.kafka.core.ProducerFactory;
// import org.springframework.stereotype.Component;
// import org.apache.kafka.clients.producer.ProducerConfig;

// @Configuration
// public class KafkaConfig {

//     @Autowired
//     private DepositorProperties properties;

//     @Value(value = "${spring.kafka.bootstrap-servers}")
//     private String bootstrapAddress;

//     @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
//     KafkaStreamsConfiguration kStreamsConfig() {
//         Map<String, Object> props = new HashMap<>();
//         props.put(APPLICATION_ID_CONFIG, "jpo-s3-depositor");
//         props.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
//         props.put(DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
//         props.put(DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

//         return new KafkaStreamsConfiguration(props);
//     }

// }
