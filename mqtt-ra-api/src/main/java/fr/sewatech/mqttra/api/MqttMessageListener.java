package fr.sewatech.mqttra.api;

/**
 * @author Alexis Hassler
 */
public interface MqttMessageListener {

    void onMessage(Message message);
}
