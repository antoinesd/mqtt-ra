package fr.sewatech.mqttra.example;

import fr.sewatech.mqttra.api.Message;
import fr.sewatech.mqttra.api.MqttListener;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;

/**
 * @author Alexis Hassler
 */
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "topicName", propertyValue = "swt2")
})
public class SecondMqttBean implements MqttListener{
    @Override
    public void onMessage(Message message) {
        System.out.println("Message received in " + this.getClass().getName() + " on Topic " + message.getTopic());
    }
}
