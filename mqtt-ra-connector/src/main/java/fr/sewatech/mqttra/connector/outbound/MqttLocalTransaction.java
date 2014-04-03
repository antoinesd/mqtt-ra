package fr.sewatech.mqttra.connector.outbound;

import javax.resource.ResourceException;
import javax.resource.spi.LocalTransaction;
import java.util.logging.Logger;

/**
 * @author Alexis Hassler
 */
public class MqttLocalTransaction implements LocalTransaction {
    private static final Logger logger = Logger.getLogger(MqttLocalTransaction.class.getName());

    @Override
    public void begin() throws ResourceException {
        logger.fine("Asked for beginning a local transaction, but nothing is done");
    }

    @Override
    public void commit() throws ResourceException {
        logger.fine("Asked for committing a local transaction, but nothing is done");
    }

    @Override
    public void rollback() throws ResourceException {
        logger.fine("Asked for rollbacking a local transaction, but nothing is done");
    }
}
