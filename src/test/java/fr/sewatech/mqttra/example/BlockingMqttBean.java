package fr.sewatech.mqttra.example;

import fr.sewatech.mqttra.api.Message;
import fr.sewatech.mqttra.api.MqttListener;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;

/**
 * @author Alexis Hassler
 */
@MessageDriven(
    activationConfig = {
        @ActivationConfigProperty(propertyName = "topicName", propertyValue = "swt1"),
        @ActivationConfigProperty(propertyName = "qosLevel", propertyValue = "2"),
        @ActivationConfigProperty(propertyName = "serverUrl", propertyValue = "tcp://localhost:1883")
    }
)
public class BlockingMqttBean implements MqttListener{

    @Override
    public void onMessage(Message message) {
        try {
            System.out.println("Message received in " + this.getClass().getSimpleName() + " : " + new String(message.getPayload()));
            Thread.sleep(1000);
            System.out.println("Blocking MDB : done");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
