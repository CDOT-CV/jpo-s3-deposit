package us.dot.its.jpo.ode.depositor;

import org.apache.kafka.streams.KafkaStreams;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import lombok.Getter;
import lombok.Setter;
import lombok.AccessLevel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;

import us.dot.its.jpo.ode.depositor.DepositorProperties;
import us.dot.its.jpo.ode.depositor.topologies.StreamsTopology;


@Controller
@DependsOn("createKafkaTopics")
@Profile("!test && !testConfig")
public class DepositorServiceController {
    private static final Logger logger = LoggerFactory.getLogger(DepositorServiceController.class);
    org.apache.kafka.common.serialization.Serdes bas;

    // Temporary for KafkaStreams that don't implement the Algorithm interface
    @Getter
    final ConcurrentHashMap<String, KafkaStreams> streamsMap = new ConcurrentHashMap<String, KafkaStreams>();

    @Getter
    final ConcurrentHashMap<String, StreamsTopology> algoMap = new ConcurrentHashMap<String, StreamsTopology>();

   
    
    @Autowired
    public DepositorServiceController(final DepositorProperties depositorProperties, 
            final KafkaTemplate<String, String> kafkaTemplate) {
                try {
                    logger.info("Starting {}", this.getClass().getSimpleName());
                    
                        
                    
        
                    
                    logger.info("All services started!");
                } catch (Exception e) {
                    logger.error("Encountered issue with creating topologies", e);
                } 

            }
}
