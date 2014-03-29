package fr.sewatech.mqttra.connector.outbound;

import javax.resource.ResourceException;
import javax.resource.spi.ManagedConnectionMetaData;

/**
 * @author Alexis Hassler
 */
public class MqttManagedConnectionMetaData implements ManagedConnectionMetaData {

    @Override
    public String getEISProductName() throws ResourceException {
        return "MQTT";
    }

    @Override
    public String getEISProductVersion() throws ResourceException {
        return "3";
    }

    @Override
    public int getMaxConnections() throws ResourceException {
        return 0;
    }

    @Override
    public String getUserName() throws ResourceException {
        return null;
    }
}