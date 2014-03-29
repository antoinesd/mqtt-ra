package fr.sewatech.mqttra.connector.outbound;

import org.fusesource.mqtt.client.QoS;

import javax.resource.spi.ConnectionRequestInfo;

/**
 * @author Alexis Hassler
 */
public class MqttConnectionRequestInfo implements ConnectionRequestInfo {
    private String serverUrl = "tcp://localhost:1883";
    private int qosLevel = 0;
    private String topicName = null;

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void setQosLevel(int qosLevel) {
        this.qosLevel = qosLevel;
    }

    public QoS getQos() {
        return QoS.values()[qosLevel];
    }


    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }
}
