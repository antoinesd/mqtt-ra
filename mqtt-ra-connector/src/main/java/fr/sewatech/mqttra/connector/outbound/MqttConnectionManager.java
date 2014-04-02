package fr.sewatech.mqttra.connector.outbound;

import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionFactory;
import javax.security.auth.Subject;
import java.io.Serializable;

public class MqttConnectionManager implements ConnectionManager, Serializable {
    public Object allocateConnection(ManagedConnectionFactory mcf, ConnectionRequestInfo cxRequestInfo) throws javax.resource.ResourceException {
        Subject subject = null;
        ManagedConnection mc = mcf.createManagedConnection(subject, cxRequestInfo);
        return mc.getConnection(subject, cxRequestInfo);
    }
}