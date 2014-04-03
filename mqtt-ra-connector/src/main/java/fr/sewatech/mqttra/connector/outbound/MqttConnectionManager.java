package fr.sewatech.mqttra.connector.outbound;

import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionFactory;
import javax.security.auth.Subject;
import java.io.Serializable;
import java.util.logging.Logger;


/**
 * Should be useful in a non-managed environment, i.e. outside of any app server. But who cares about JCA outside of app servers ?
 *
 */
public class MqttConnectionManager implements ConnectionManager, Serializable {

    private static final Logger logger = Logger.getLogger(MqttConnectionManager.class.getName());

    public Object allocateConnection(ManagedConnectionFactory mcf, ConnectionRequestInfo cxRequestInfo) throws javax.resource.ResourceException {
        logger.fine("Allocating connection");
        Subject subject = null;
        ManagedConnection mc = mcf.createManagedConnection(subject, cxRequestInfo);
        return mc.getConnection(subject, cxRequestInfo);
    }
}