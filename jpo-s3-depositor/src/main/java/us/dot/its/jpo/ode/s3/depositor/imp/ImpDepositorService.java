package us.dot.its.jpo.ode.s3.depositor.imp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.ByteString;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.dot.its.jpo.ode.depositor.GeoRoutedMsg;
import us.dot.its.jpo.ode.depositor.GeoRoutedMsgOrBuilder;
import us.dot.its.jpo.ode.depositor.GeoRoutedMsg_PB2;
import us.dot.its.jpo.ode.model.OdeBsmData;
import us.dot.its.jpo.ode.model.OdeBsmMetadata;
import us.dot.its.jpo.ode.model.OdeBsmPayload;
import us.dot.its.jpo.ode.model.OdeTimData;
import us.dot.its.jpo.ode.model.OdeTimMetadata;
import us.dot.its.jpo.ode.model.OdeTimPayload;
import us.dot.its.jpo.ode.plugin.j2735.OdeTravelerInformationMessage;
import us.dot.its.jpo.ode.s3.depositor.DateJsonMapper;

import org.springframework.kafka.annotation.KafkaListener;
import us.dot.its.jpo.ode.model.OdeMsgMetadata.GeneratedBy;

// @Service
public class ImpDepositorService {
    private static final Logger logger = LoggerFactory.getLogger(ImpDepositorService.class);

    private final ObjectMapper mapper = DateJsonMapper.getInstance();

    // TODO: Use the filtered TMC topic instead of the mixed TIM topic
    @KafkaListener(topics = "topic.OdeTimJson", groupId = "jpo-s3-depositor", concurrency = "${listen.concurrency:3}")
    public void listenTopic1(String message) {
        try {
            OdeTimData timMsg = mapper.readValue(message, OdeTimData.class);
            OdeTimMetadata timMetadata = (OdeTimMetadata) timMsg.getMetadata();
            
            if (timMetadata.getRecordGeneratedBy().equals(GeneratedBy.T)) {

            }

            OdeTimPayload timPayload = (OdeTimPayload) timMsg.getPayload();
            OdeTravelerInformationMessage timData = (OdeTravelerInformationMessage) timPayload.getData();
            timData.get

            timPayload.getTim().getRecord().get(0).getFrameType();

            logger.info("Received message: " + pojo1.getMetadata().getAsn1());
        } catch (Exception e) {
            // Handle exception
        }
    }

}
