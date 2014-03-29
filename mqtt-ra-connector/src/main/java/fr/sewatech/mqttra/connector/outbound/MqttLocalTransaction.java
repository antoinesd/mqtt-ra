package fr.sewatech.mqttra.connector.outbound;

import javax.resource.ResourceException;
import javax.resource.spi.LocalTransaction;

/**
 * @author Alexis Hassler
 */
public class MqttLocalTransaction implements LocalTransaction {
    @Override
    public void begin() throws ResourceException {

    }

    @Override
    public void commit() throws ResourceException {

    }

    @Override
    public void rollback() throws ResourceException {

    }
}
