package fr.sewatech.mqttra.connector.outbound;

import fr.sewatech.mqttra.api.MqttConnection;
import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.QoS;

/**
 * @author Alexis Hassler
 */
public class MqttBlockingConnectionImpl implements MqttConnection {
    private BlockingConnection blockingConnection;
    private QoS defaultQos;
    private String defaultTopic;

    public MqttBlockingConnectionImpl(BlockingConnection blockingConnection) {
        this.blockingConnection = blockingConnection;
    }

    @Override
    public void publish(String topicName, String message, QoS qos) {
        try {
            System.out.println("Trying to publish message " + message + " on topic " + topicName);
            blockingConnection.publish(topicName, message.getBytes(), qos, true);
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
