package us.dot.its.jpo.ode.s3.depositor;

import java.lang.management.ManagementFactory;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import us.dot.its.jpo.ode.s3.depositor.DepositorProperties;

@SpringBootApplication
@EnableKafka
@EnableConfigurationProperties(DepositorProperties.class)
public class DepositorApplication {
	private static final Logger logger = LoggerFactory.getLogger(DepositorApplication.class);

	static final int DEFAULT_NO_THREADS = 10;
	static final String DEFAULT_SCHEMA = "default";

	public static void main(String[] args) throws MalformedObjectNameException, InterruptedException,
			InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {

		SpringApplication.run(DepositorApplication.class, args);
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		SystemConfig mBean = new SystemConfig(DEFAULT_NO_THREADS, DEFAULT_SCHEMA);
		ObjectName name = new ObjectName("us.dot.its.jpo.ode.depositor:type=SystemConfig");
		mbs.registerMBean(mBean, name);
	}

	@KafkaListener(topics = "topic1, topic2", groupId = "foo")
	public void listen(String message) {
		logger.info("Received message in group foo: " + message);
	}

}
