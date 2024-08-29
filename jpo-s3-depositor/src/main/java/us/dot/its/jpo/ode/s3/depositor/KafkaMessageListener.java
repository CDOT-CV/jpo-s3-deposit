package us.dot.its.jpo.ode.s3.depositor;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.dot.its.jpo.ode.model.OdeBsmData;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaMessageListener {
    private static final Logger logger = LoggerFactory.getLogger(KafkaMessageListener.class);

    private final ObjectMapper mapper = DateJsonMapper.getInstance();

    @KafkaListener(topics = "topic.OdeBsmJson", groupId = "jpo-s3-depositor", concurrency = "${listen.concurrency:3}")
    public void listenTopic1(String message) {
        try {
            OdeBsmData pojo1 = mapper.readValue(message, OdeBsmData.class);
            logger.info("Received message: " + pojo1.getMetadata().getRecordGeneratedAt());
        } catch (Exception e) {
            // Handle exception
        }
    }

}
