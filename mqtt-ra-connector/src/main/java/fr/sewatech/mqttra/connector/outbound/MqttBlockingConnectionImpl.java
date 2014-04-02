package fr.sewatech.mqttra.connector.outbound;

import fr.sewatech.mqttra.api.MqttConnection;
import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;

import javax.resource.ResourceException;

/**
 * @author Alexis Hassler
 */
public class MqttBlockingConnectionImpl implements MqttConnection {
    private BlockingConnection connection;
    private QoS defaultQos;
    private String defaultTopic;

    public MqttBlockingConnectionImpl(MqttConnectionRequestInfo connectionRequestInfo) throws ResourceException {
        this.setDefaultQos(connectionRequestInfo.getQos());
        this.setDefaultTopic(connectionRequestInfo.getTopicName());

        try {
            MQTT mqtt = new MQTT();
            mqtt.setHost(connectionRequestInfo.getServerUrl());
            mqtt.setUserName(connectionRequestInfo.getUserName());
            mqtt.setPassword(connectionRequestInfo.getPassword());
            connection = mqtt.blockingConnection();
            connection.connect();
        } catch (Exception e) {
            throw new ResourceException(e);
        }

    }

    @Override
    public void publish(String topicName, String message, QoS qos) {
        try {
            System.out.println("Trying to publish message " + message + " on topic " + topicName);
            connection.publish(topicName, message.getBytes(), qos, true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void publish(String topicName, String message) {
        publish(topicName, message, defaultQos);
    }

    @Override
    public void publish(String message) {
        publish(defaultTopic, message);
    }

    public void setDefaultQos(QoS defaultQos) {
        this.defaultQos = defaultQos;
    }

    public void setDefaultTopic(String defaultTopic) {
        this.defaultTopic = defaultTopic;
    }
}
