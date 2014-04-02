package fr.sewatech.mqttra.api;

/**
 * @author Alexis Hassler
 */
public interface MqttConnectionFactory {
    MqttConnection getConnection();

    MqttConnection getConnection(String userName, String password);
}
