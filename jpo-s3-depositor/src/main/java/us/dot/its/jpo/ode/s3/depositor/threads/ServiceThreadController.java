package us.dot.its.jpo.ode.s3.depositor.threads;

import org.apache.commons.lang3.SystemUtils;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import us.dot.its.jpo.ode.s3.depositor.DepositorProperties;
import us.dot.its.jpo.ode.s3.depositor.imp.ImpRegistration;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Controller
public class ServiceThreadController {
    private Logger logger = LoggerFactory.getLogger(ServiceThreadController.class);
    // DepositorProperties depositorProperties;
    String kafkaType;

    @Autowired
    public ServiceThreadController(DepositorProperties depositorProperties) {
        logger.info("Starting ServiceThreadController");
        // this.depositorProperties = depositorProperties;
        // this.kafkaType = depositorProperties.getKafkaType();

        var sm = new ServiceManager(new ServiceThreadFactory("ServiceManager"));

        if (depositorProperties.getImpEnabled()) {
            startImpServices(depositorProperties);
        }

        logger.info("All services started!");

    }

    public void startImpServices(DepositorProperties depositorProperties) {
        logger.info("Starting IMP services");
        // sm.startServices();

        var impRegistrationService = new ImpRegistration(depositorProperties);
        var response = impRegistrationService.registerClientPartner();

        var sm = new ServiceManager(new ServiceThreadFactory("ServiceManager"));

    }

}
