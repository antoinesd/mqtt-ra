package fr.sewatech.mqttra.connector.outbound;

import fr.sewatech.mqttra.api.MqttConnection;
import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.QoS;

/**
 * @author Alexis Hassler
 */
public class MqttBlockingConnectionImpl implements MqttConnection {
    private BlockingConnection blockingConnection;

    public MqttBlockingConnectionImpl(BlockingConnection blockingConnection) {
        this.blockingConnection = blockingConnection;
    }

    @Override
    public void publish(String topicName, String message) {
        try {
            blockingConnection.publish(topicName, message.getBytes(), QoS.EXACTLY_ONCE, true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
