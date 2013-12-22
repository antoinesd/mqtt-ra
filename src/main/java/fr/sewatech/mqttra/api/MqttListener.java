package fr.sewatech.mqttra.api;

import org.fusesource.mqtt.client.Message;

/**
 * @author Alexis Hassler
 */
public interface MqttListener {

    void onMessage(Message message);
}
