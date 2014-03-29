package fr.sewatech.mqttra.example;

import fr.sewatech.mqttra.api.Message;
import fr.sewatech.mqttra.api.MqttMessageListener;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;

/**
 * @author Alexis Hassler
 */
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "topicName", propertyValue = "swt2")
})
public class SecondMqttBean implements MqttMessageListener {

    @Override
    public void onMessage(Message message) {
        Messages.add(message);
        System.out.println("Message received in " + this.getClass().getName() + " on Topic " + message.getTopic());
    }
}
