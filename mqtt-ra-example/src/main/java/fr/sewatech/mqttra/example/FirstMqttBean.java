package fr.sewatech.mqttra.example;

import fr.sewatech.mqttra.api.Message;
import fr.sewatech.mqttra.api.MqttConnection;
import fr.sewatech.mqttra.api.MqttConnectionFactory;
import fr.sewatech.mqttra.api.MqttMessageListener;
import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.QoS;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.naming.*;

/**
 * @author Alexis Hassler
 */
@MessageDriven(
    activationConfig = {
        @ActivationConfigProperty(propertyName = "topicName", propertyValue = "swt/Question"),
        @ActivationConfigProperty(propertyName = "qosLevel", propertyValue = "2"),
        @ActivationConfigProperty(propertyName = "serverUrl", propertyValue = "tcp://localhost:1883")
    }
)
public class FirstMqttBean implements MqttMessageListener {

    @Resource(name="MqttDashboardCF")
    MqttConnectionFactory connectionFactory;

    @Override
    public void onMessage(Message message) {
        Messages.add(message);
        System.out.println("Message received " + new String(message.getPayload()) + " in " + this.getClass().getName() + " on Topic " + message.getTopic());

        //JndiUtil.inspect("java:");

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
