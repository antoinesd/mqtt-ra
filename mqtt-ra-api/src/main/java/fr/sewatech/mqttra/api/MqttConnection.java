package fr.sewatech.mqttra.api;

/**
 * @author Alexis Hassler
 */
public interface MqttConnection {
    void publish(String topicName, String message);
}
