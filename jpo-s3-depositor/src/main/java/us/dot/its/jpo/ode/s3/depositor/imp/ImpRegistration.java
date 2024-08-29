package us.dot.its.jpo.ode.s3.depositor.imp;

import us.dot.its.jpo.ode.s3.depositor.DateJsonMapper;
import us.dot.its.jpo.ode.s3.depositor.DepositorProperties;
import us.dot.its.jpo.ode.s3.depositor.models.imp.AuthToken;
import us.dot.its.jpo.ode.s3.depositor.models.imp.AuthTokenRequest;
import us.dot.its.jpo.ode.s3.depositor.models.imp.ClientCompleteResponse;
import us.dot.its.jpo.ode.s3.depositor.models.imp.ClientConnectionPostRequest;
import us.dot.its.jpo.ode.s3.depositor.models.imp.ClientConnectionResponse;
import us.dot.its.jpo.ode.s3.depositor.models.imp.ClientRegistrationConnectionPostRequest;
import us.dot.its.jpo.ode.s3.depositor.models.imp.ClientRegistrationPostRequest;
import us.dot.its.jpo.ode.s3.depositor.models.imp.ClientRegistrationResponse;
import us.dot.its.jpo.ode.s3.depositor.models.imp.ConfigData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class ImpRegistration {
    private static final Logger logger = LoggerFactory.getLogger(ImpRegistration.class);

    @Autowired
    private DepositorProperties properties;
    private ObjectMapper objectMapper;
    private RestTemplate restTemplate;

    public ImpRegistration(DepositorProperties depositorProperties) {
        this.properties = depositorProperties;
        this.restTemplate = new RestTemplate();
        this.objectMapper = DateJsonMapper.getInstance();
        this.restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    }

    public ConfigData registerClientPartner() {
        try {
            ConfigData configData;
            String deviceID = null;

            String token = getToken();
            String configPath = properties.getImpCertPath() + "/config.json";
            String caCertPath = properties.getImpCertPath() + "/imp-ca.pem";
            String certPath = properties.getImpCertPath() + "/imp-cert.pem";
            String keyPath = properties.getImpCertPath() + "/imp-key.pem";

            if (!validRegistration(configPath)) {
                logger.info("Registering client partner");

                ClientRegistrationResponse registrationResponse = register(token);

                writeToFile(caCertPath, registrationResponse.getCertificate().getCaPem());
                writeToFile(certPath, registrationResponse.getCertificate().getCertPem());
                writeToFile(keyPath, registrationResponse.getCertificate().getKeyPem());

                deviceID = registrationResponse.getDeviceID();
            } else {
                logger.info("Client partner already registered");
                String configDataJson = Files.readString(Paths.get(configPath));
                configData = objectMapper.readValue(configDataJson, ConfigData.class);
                deviceID = configData.getDeviceID();
            }

            ClientConnectionResponse connectionResponse = connection(token, deviceID);

            URI uri = new URI(connectionResponse.getMqttURL());
            String host = uri.getHost();
            int port = uri.getPort();
            configData = new ConfigData(configPath, caCertPath, certPath, keyPath, properties.getImpVendor(),
                    properties.getImpNetworkType(), host, port, deviceID);

            String configDataJson = objectMapper.writeValueAsString(configData);

            writeToFile(configPath, configDataJson);

            return configData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private String getToken() {
        var request = new AuthTokenRequest(properties.getImpPartnerUser(), properties.getImpPartnerPass());

        AuthToken response = restTemplate.postForObject(properties.getImpPartnerApiBaseUri() + "/auth/token", request,
                AuthToken.class);

        return response.getAccessToken();
    }

    private ClientRegistrationResponse register(String token) {
        ClientRegistrationPostRequest request = new ClientRegistrationPostRequest(properties.getImpClientType(),
                properties.getImpClientSubType());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("Content-Type", "application/json");

        HttpEntity<ClientRegistrationPostRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<ClientRegistrationResponse> response = restTemplate.exchange(
                    properties.getImpPartnerApiBaseUri() + "/prd/v1/registration", HttpMethod.POST, entity,
                    ClientRegistrationResponse.class);

            return response.getBody();
        } catch (HttpClientErrorException.Unauthorized e) {
            logger.error("Unauthorized error: " + e.getStackTrace());
            return null;
        }
    }

    private ClientConnectionResponse connection(String token, String deviceID) {
        ClientConnectionPostRequest request = new ClientConnectionPostRequest(deviceID, properties.getImpMecLatitude(),
                properties.getImpMecLongitude(), properties.getImpNetworkType());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("Content-Type", "application/json");

        HttpEntity<ClientConnectionPostRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<ClientConnectionResponse> response = restTemplate.exchange(
                    properties.getImpPartnerApiBaseUri() + "/prd/v1/connection", HttpMethod.POST, entity,
                    ClientConnectionResponse.class);

            return response.getBody();
        } catch (HttpClientErrorException.Unauthorized e) {
            logger.error("Unauthorized error: " + e.getStackTrace());
            return null;
        }
    }

    private void writeToFile(String filePath, String content) {
        try {
            Files.createDirectories(Paths.get(filePath).getParent());
            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(content);
            }
        } catch (IOException e) {
            logger.error("writeToFile error: " + e.getStackTrace());
        }
    }

    private boolean validRegistration(String configPath) {
        boolean valid = false;
        File file = new File(configPath);
        if (file.exists()) {
            try {
                String configDataJson = Files.readString(Paths.get(configPath));
                ConfigData configData = objectMapper.readValue(configDataJson, ConfigData.class);

                if (configData.getDeviceID() != null && configData.getNetworkType() == properties.getImpNetworkType()) {
                    valid = true;
                }
            } catch (IOException e) {
                logger.error("validRegistration error: " + e.getStackTrace());
            }
        }
        return valid;
    }
}