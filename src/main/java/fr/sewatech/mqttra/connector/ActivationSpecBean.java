package fr.sewatech.mqttra.connector;

import org.fusesource.mqtt.client.QoS;

import javax.resource.ResourceException;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.InvalidPropertyException;
import javax.resource.spi.ResourceAdapter;
import java.util.Objects;

/**
 * @author Alexis Hassler
 */
public class ActivationSpecBean implements ActivationSpec {
    private ResourceAdapter resourceAdapter;
    private String topicName = "sewatech";
    private QoS qoS = QoS.AT_MOST_ONCE;
    private String serverUrl = "tcp://localhost:1883";

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public QoS getQoS() {
        return qoS;
    }

    public void setQoS(QoS qoS) {
        this.qoS = qoS;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    @Override
    public void validate() throws InvalidPropertyException {

    }

    @Override
    public ResourceAdapter getResourceAdapter() {
        return resourceAdapter;
    }

    @Override
    public void setResourceAdapter(ResourceAdapter resourceAdapter) throws ResourceException {
        this.resourceAdapter = resourceAdapter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActivationSpecBean that = (ActivationSpecBean) o;
        return Objects.equals(this.qoS, that.qoS)
                && Objects.equals(this.serverUrl, that.serverUrl)
                && Objects.equals(this.topicName, that.topicName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topicName, qoS, serverUrl);
    }
}
