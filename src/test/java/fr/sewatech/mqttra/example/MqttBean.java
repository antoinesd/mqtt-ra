package fr.sewatech.mqttra.example;

import fr.sewatech.mqttra.api.MqttListener;
import org.fusesource.mqtt.client.Message;

import javax.ejb.MessageDriven;

/**
 * @author Alexis Hassler
 */

@MessageDriven
public class MqttBean implements MqttListener{
    @Override
    public void onMessage(Message message) {
        System.out.println("Message received in " + this.getClass().getName());
    }
}
