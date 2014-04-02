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
    private String userName;
    private String password;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public MqttConnectionRequestInfo mergeWith(MqttConnectionRequestInfo connectionRequestInfo) {
        if (isNullOrEmpty(serverUrl)) {
            serverUrl = connectionRequestInfo.serverUrl;
        }
        if (isNullOrEmpty(topicName)) {
            topicName = connectionRequestInfo.topicName;
        }
        if (isNullOrEmpty(userName)) {
            userName = connectionRequestInfo.userName;
        }
        if (isNullOrEmpty(password)) {
            password = connectionRequestInfo.password;
        }
        return this;
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }
}
