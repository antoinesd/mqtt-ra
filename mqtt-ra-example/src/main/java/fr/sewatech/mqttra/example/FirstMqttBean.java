package fr.sewatech.mqttra.example;

import fr.sewatech.mqttra.api.Message;
import fr.sewatech.mqttra.api.MqttConnection;
import fr.sewatech.mqttra.api.MqttConnectionFactory;
import fr.sewatech.mqttra.api.MqttMessageListener;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;

/**
 * @author Alexis Hassler
 */
@MessageDriven(
    activationConfig = {
        @ActivationConfigProperty(propertyName = "topicName", propertyValue = "swt/Question"),
        @ActivationConfigProperty(propertyName = "qosLevel",  propertyValue = "1"),
        @ActivationConfigProperty(propertyName = "serverUrl", propertyValue = "tcp://localhost:1883"),
        @ActivationConfigProperty(propertyName = "userName",  propertyValue = "user"),
        @ActivationConfigProperty(propertyName = "password",  propertyValue = "password")
    }
)
public class FirstMqttBean implements MqttMessageListener {

    public static final String RA_JNDI_NAME = "${mqttra.jndiname}";

    @Resource(name= RA_JNDI_NAME)
    MqttConnectionFactory connectionFactory;

    @Override
    public void onMessage(Message message) {
        Messages.add(message);
        System.out.println("Message received " + new String(message.getPayload()) + " in " + this.getClass().getName() + " on Topic " + message.getTopic());

        answer("OK");
    }

    private void answer(String message) {
        try {
            MqttConnection connection = connectionFactory.getConnection();
            connection.publish(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
