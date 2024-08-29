package us.dot.its.jpo.ode.s3.depositor;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.UUID;

import jakarta.annotation.PostConstruct;

import org.apache.commons.lang3.SystemUtils;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
// import org.apache.kafka.streams.StreamsConfig;
// import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;
// import org.apache.kafka.streams.processor.LogAndSkipOnInvalidTimestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import lombok.Setter;
import us.dot.its.jpo.ode.s3.depositor.utils.CommonUtils;
import lombok.AccessLevel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

@Getter
@Setter
@ConfigurationProperties
public class DepositorProperties implements EnvironmentAware {
	private static final Logger logger = LoggerFactory.getLogger(DepositorProperties.class);

	@Autowired
	@Setter(AccessLevel.NONE)
	private Environment env;

	// Confluent Properties
	private boolean confluentCloudEnabled = false;
	private String confluentKey = null;
	private String confluentSecret = null;

	public Boolean getConfluentCloudStatus() {
		return confluentCloudEnabled;
	}

	/*
	 * General Properties
	 */
	private String version;

	@Value("${spring.kafka.bootstrap-servers}")
	private String kafkaBrokers = null;

	@Value("${spring.kafka.consumer.group-id}")
	private String groupId;

	private static final String DEFAULT_KAFKA_PORT = "9092";

	@Setter(AccessLevel.NONE)
	private String hostId;

	@Setter(AccessLevel.NONE)
	private String kafkaBrokerIP = null;

	@Setter(AccessLevel.NONE)
	private String kafkaTopics = null;

	/*
	 * S3 Properties
	 */
	// private String K_AWS_ACCESS_KEY_ID;
	// private String K_AWS_SECRET_ACCESS_KEY;
	// private String K_AWS_SESSION_TOKEN;
	// private String K_AWS_EXPIRATION;
	// private String API_ENDPOINT;
	// private String HEADER_Accept;
	// private String HEADER_X_API_KEY;

	// private String AWS_ACCESS_KEY_ID;
	// private String AWS_SECRET_ACCESS_KEY;
	// private String AWS_SESSION_TOKEN;
	// private String AWS_EXPIRATION;

	// private String S3_BUCKET_NAME;
	// private String S3_KEY_NAME;
	// private String AWS_REGION_NAME;

	/*
	 * IMP Properties
	 */
	private String impVendor;
	private String impNetworkType;
	private String impPartnerApiBaseUri;
	private String impPartnerUser;
	private String impPartnerPass;
	private String impClientType;
	private String impClientSubType;
	private String impTopicType;
	private String impCacheRegistration;
	private String impMecLatitude;
	private String impMecLongitude;

	@Setter(AccessLevel.NONE)
	@Autowired
	BuildProperties buildProperties;

	@PostConstruct
	void initialize() {
		setVersion(buildProperties.getVersion());
		logger.info("groupId: {}", buildProperties.getGroup());
		logger.info("artifactId: {}", buildProperties.getArtifact());
		logger.info("version: {}", version);

		String hostname;
		try {
			hostname = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			// Let's just use a random hostname
			hostname = UUID.randomUUID().toString();
			logger.info("Unknown host error: {}, using random", e);
		}

		hostId = hostname;
		logger.info("Host ID: {}", hostId);
		logger.info("Initializing services on host {}", hostId);

		if (kafkaBrokers == null) {

			String kafkaBroker = CommonUtils.getEnvironmentVariable("KAFKA_BROKER_IP");

			logger.info("ode.kafkaBrokers property not defined. Will try KAFKA_BROKER_IP => {}", kafkaBrokers);

			if (kafkaBroker == null) {
				logger.warn(
						"Neither ode.kafkaBrokers ode property nor KAFKA_BROKER_IP environment variable are defined. Defaulting to localhost.");
				kafkaBroker = "localhost";
			}

			kafkaBrokers = kafkaBroker + ":" + DEFAULT_KAFKA_PORT;
		}

		String kafkaType = CommonUtils.getEnvironmentVariable("KAFKA_TYPE");
		if (kafkaType != null) {
			confluentCloudEnabled = kafkaType.equals("CONFLUENT");
			if (confluentCloudEnabled) {

				System.out.println("Enabling Confluent Cloud Integration");

				confluentKey = CommonUtils.getEnvironmentVariable("CONFLUENT_KEY");
				confluentSecret = CommonUtils.getEnvironmentVariable("CONFLUENT_SECRET");
			}
		}

		// S3 Properties
		// S3_BUCKET_NAME = CommonUtils.getEnvironmentVariable("DEPOSIT_BUCKET_NAME",
		// "");
		// AWS_REGION_NAME = CommonUtils.getEnvironmentVariable("REGION", "us-east-1");
		// S3_KEY_NAME = CommonUtils.getEnvironmentVariable("DEPOSIT_KEY_NAME", "");

		// K_AWS_ACCESS_KEY_ID = CommonUtils.getEnvironmentVariable("AWS_ACCESS_KEY_ID",
		// "AccessKeyId");
		// K_AWS_SECRET_ACCESS_KEY =
		// CommonUtils.getEnvironmentVariable("AWS_SECRET_ACCESS_KEY",
		// "SecretAccessKey");
		// K_AWS_SESSION_TOKEN = CommonUtils.getEnvironmentVariable("AWS_SESSION_TOKEN",
		// "SessionToken");
		// K_AWS_EXPIRATION = CommonUtils.getEnvironmentVariable("AWS_EXPIRATION",
		// "Expiration");
		// API_ENDPOINT = CommonUtils.getEnvironmentVariable("API_ENDPOINT", "");
		// HEADER_Accept = CommonUtils.getEnvironmentVariable("HEADER_ACCEPT",
		// "application/json");
		// HEADER_X_API_KEY = CommonUtils.getEnvironmentVariable("HEADER_X_API_KEY",
		// "");

		impVendor = CommonUtils.getEnvironmentVariable("IMP_VENDOR");
		impNetworkType = CommonUtils.getEnvironmentVariable("IMP_NETWORK_TYPE");
		impPartnerApiBaseUri = CommonUtils.getEnvironmentVariable("IMP_PARTNER_API_BASE_URI");
		impPartnerUser = CommonUtils.getEnvironmentVariable("IMP_PARTNER_USER");
		impPartnerPass = CommonUtils.getEnvironmentVariable("IMP_PARTNER_PASS");
		impClientType = CommonUtils.getEnvironmentVariable("IMP_CLIENT_TYPE");
		impClientSubType = CommonUtils.getEnvironmentVariable("IMP_CLIENT_SUB_TYPE");
		impMecLatitude = CommonUtils.getEnvironmentVariable("IMP_MEC_LATITUDE");
		impMecLongitude = CommonUtils.getEnvironmentVariable("IMP_MEC_LONGITUDE");

	}

	// public Properties createStreamProperties(String name) {
	// Properties streamProps = new Properties();
	// streamProps.put(StreamsConfig.APPLICATION_ID_CONFIG, name);

	// streamProps.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokers);

	// streamProps.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG,
	// LogAndContinueExceptionHandler.class.getName());

	// streamProps.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG,
	// LogAndSkipOnInvalidTimestamp.class.getName());

	// streamProps.put(StreamsConfig.DEFAULT_PRODUCTION_EXCEPTION_HANDLER_CLASS_CONFIG,
	// AlwaysContinueProductionExceptionHandler.class.getName());

	// streamProps.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 2);

	// // streamProps.put(StreamsConfig.producerPrefix("acks"), "all");
	// streamProps.put(StreamsConfig.producerPrefix(ProducerConfig.ACKS_CONFIG),
	// "all");

	// // Reduce cache buffering per topology to 1MB
	// streamProps.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 1 * 1024 *
	// 1024L);

	// // Decrease default commit interval. Default for 'at least once' mode of
	// 30000ms
	// // is too slow.
	// streamProps.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 100);

	// // All the keys are Strings in this app
	// streamProps.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,
	// Serdes.String().getClass().getName());

	// // Configure the state store location
	// if (SystemUtils.IS_OS_LINUX) {
	// streamProps.put(StreamsConfig.STATE_DIR_CONFIG,
	// "/var/lib/ode/kafka-streams");
	// } else if (SystemUtils.IS_OS_WINDOWS) {
	// streamProps.put(StreamsConfig.STATE_DIR_CONFIG, "C:/temp/ode");
	// }
	// // streamProps.put(StreamsConfig.STATE_DIR_CONFIG, "/var/lib/")\

	// // Increase max.block.ms and delivery.timeout.ms for streams
	// final int FIVE_MINUTES_MS = 5 * 60 * 1000;
	// streamProps.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, FIVE_MINUTES_MS);
	// streamProps.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, FIVE_MINUTES_MS);

	// // Disable batching
	// streamProps.put(ProducerConfig.BATCH_SIZE_CONFIG, 0);

	// if (confluentCloudEnabled) {
	// streamProps.put("ssl.endpoint.identification.algorithm", "https");
	// streamProps.put("security.protocol", "SASL_SSL");
	// streamProps.put("sasl.mechanism", "PLAIN");

	// if (confluentKey != null && confluentSecret != null) {
	// String auth = "org.apache.kafka.common.security.plain.PlainLoginModule
	// required " + "username=\""
	// + confluentKey + "\" " + "password=\"" + confluentSecret + "\";";
	// streamProps.put("sasl.jaas.config", auth);
	// } else {
	// logger.error(
	// "Environment variables CONFLUENT_KEY and CONFLUENT_SECRET are not set. Set
	// these in the .env file to use Confluent Cloud");
	// }
	// }

	// return streamProps;
	// }

	public String getProperty(String key) {
		return env.getProperty(key);
	}

	public String getProperty(String key, String defaultValue) {
		return env.getProperty(key, defaultValue);
	}

	public Object getProperty(String key, int i) {
		return env.getProperty(key, Integer.class, i);
	}

	@Value("${spring.kafka.bootstrap-servers}")
	public void setKafkaBrokers(String kafkaBrokers) {
		this.kafkaBrokers = kafkaBrokers;
	}

	@Override
	public void setEnvironment(Environment environment) {
		env = environment;
	}
}
