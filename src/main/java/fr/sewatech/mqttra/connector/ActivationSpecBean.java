package fr.sewatech.mqttra.connector;

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

    private String topicName;
    private int qos = 0;
    private String serverUrl = "tcp://localhost:1883";

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public int getQos() {
        return qos;
    }

    public void setQos(int qos) {
        this.qos = qos;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    @Override
    public void validate() throws InvalidPropertyException {
        validateNotNullOrEmpty("topicName", topicName);
        validateNotNullOrEmpty("serverUrl", serverUrl);
        if (qos < 0 || qos > 2) {
            throw new InvalidPropertyException("qos value " + qos + "is not valid, it should be between 0 and 2");
        }
    }

    private void validateNotNullOrEmpty(String propertyName, String value) throws InvalidPropertyException {
        if (value == null || value.isEmpty()) {
            throw new InvalidPropertyException(propertyName + " is required");
        }
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
        return Objects.equals(this.qos, that.qos)
                && Objects.equals(this.serverUrl, that.serverUrl)
                && Objects.equals(this.topicName, that.topicName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topicName, qos, serverUrl);
    }
}
