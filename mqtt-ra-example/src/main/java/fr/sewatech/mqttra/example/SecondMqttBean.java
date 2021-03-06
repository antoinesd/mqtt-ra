package fr.sewatech.mqttra.example;

import fr.sewatech.mqttra.api.Message;
import fr.sewatech.mqttra.api.MqttConnectionFactory;
import fr.sewatech.mqttra.api.MqttMessageListener;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;

/**
 * @author Alexis Hassler
 */
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "topicName", propertyValue = "swt/Question")
})
public class SecondMqttBean implements MqttMessageListener {

    @Inject
    MqttConnectionFactory connectionFactory;

    @Override
    public void onMessage(Message message) {
        Messages.add(message);
        System.out.println("Message received in " + this.getClass().getName() + " on Topic " + message.getTopic());
    }
}
