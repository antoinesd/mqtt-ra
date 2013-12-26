package fr.sewatech.mqttra.example;

import fr.sewatech.mqttra.api.Message;
import fr.sewatech.mqttra.api.MqttListener;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;

/**
 * @author Alexis Hassler
 */
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "topicName", propertyValue = "swt1"),
        @ActivationConfigProperty(propertyName = "qos", propertyValue = "2"),
        @ActivationConfigProperty(propertyName = "serverUrl", propertyValue = "tcp://localhost:1883")
})
public class FirstMqttBean implements MqttListener{

    @Override
    public void onMessage(Message message) {
        Messages.add(message);
        System.out.println("Message received in " + this.getClass().getName() + " on Topic " + message.getTopic());
    }
}
