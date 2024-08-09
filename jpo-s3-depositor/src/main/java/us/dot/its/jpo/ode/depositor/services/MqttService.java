package us.dot.its.jpo.ode.depositor.services;



@Service
public class MqttService {

    private final IMqttClient mqttClient;

    public MqttService() throws MqttException {
        mqttClient = new MqttClient("tcp://broker.hivemq.com:1883", MqttClient.generateClientId());
        mqttClient.connect();
    }

    public void publish(String topic, String message) {
        MqttMessage mqttMessage = new MqttMessage(message.getBytes());
        mqttMessage.setQos(2);
        mqttClient.publish(topic, mqttMessage);
    }

    @PreDestroy
    public void cleanup() throws MqttException {
        if (mqttClient.isConnected()) {
            mqttClient.disconnect();
        }
    }
}