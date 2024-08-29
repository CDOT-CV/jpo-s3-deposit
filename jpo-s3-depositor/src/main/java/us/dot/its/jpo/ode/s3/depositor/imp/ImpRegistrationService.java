package us.dot.its.jpo.ode.s3.depositor.imp;

import us.dot.its.jpo.ode.s3.depositor.DepositorProperties;
import us.dot.its.jpo.ode.s3.depositor.pojos.imp.ImpConfigData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class ImpRegistrationService {

    @Autowired
    private DepositorProperties depositorProperties;

    private final String BASE_URI = depositorProperties.getImpPartnerApiBaseUri();

    public ImpConfigData registerClientPartner(ImpConfigData configData) {
        String token = getToken();
        Map<String, Object> response = registerAndConnection(configData, token);

        Map<String, Object> responseDict = (Map<String, Object>) response.get("registration");

        Map<String, String> certDict = (Map<String, String>) responseDict.get("certificates");
        String deviceId = (String) responseDict.get("deviceID");
        configData.setDeviceID(deviceId);

        String outDir = configData.getOutputDirectory();

        writeToFile(outDir + "/" + configData.getCaCert(), certDict.get("caCert"));
        writeToFile(outDir + "/" + configData.getClientCert(), certDict.get("clientCert"));
        writeToFile(outDir + "/" + configData.getKeyFile(), certDict.get("keyFile"));

        String impUrl = (String) ((Map<String, Object>) response.get("connection")).get("MqttURL").toString();
        configData.setImpIpAddress(impUrl.split(":")[0]);
        configData.setImpPort(Integer.parseInt(impUrl.split(":")[1]));

        writeToConfigData(configData);

        return configData;
    }

    private String getToken() {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> payload = new HashMap<>();
        payload.put("username", depositorProperties.getImpPartnerUser());
        payload.put("password", depositorProperties.getImpPartnerPass());

        Map<String, String> response = restTemplate.postForObject(BASE_URI + "/auth/token", payload, Map.class);

        return response.get("access_token");
    }

    private Map<String, Object> registerAndConnection(ImpConfigData configData, String token) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> payload = new HashMap<>();
        payload.put("ClientType", configData.getClientType());
        payload.put("ClientSubtype", configData.getClientSubType());
        payload.put("NetworkType", configData.getNetworkType());
        payload.put("lat", configData.getDeviceFixedLocationLat());
        payload.put("long", configData.getDeviceFixedLocationLong());

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);

        return restTemplate.postForObject(BASE_URI + "/prd/v1/registration-connection", payload, Map.class, headers);
    }

    private void writeToFile(String filePath, String content) {
        try {
            Files.createDirectories(Paths.get(filePath).getParent());
            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToConfigData(ImpConfigData configData) {
        // Implement logic to write configData to a file
    }
}