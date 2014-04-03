package fr.sewatech.mqttra.connector.outbound;

import fr.sewatech.mqttra.api.MqttConnection;
import fr.sewatech.mqttra.api.MqttConnectionFactory;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.resource.Referenceable;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ManagedConnectionFactory;
import java.util.logging.Logger;

/**
 * @author Alexis Hassler
 */
public class MqttConnectionFactoryImpl implements Referenceable, MqttConnectionFactory {
    private static final Logger logger = Logger.getLogger(MqttConnectionFactoryImpl.class.getName());

    private final ManagedConnectionFactory managedConnectionFactory;
    private final ConnectionManager cxManager;
    private Reference reference;

    public MqttConnectionFactoryImpl(ManagedConnectionFactory managedConnectionFactory, ConnectionManager cxManager) {
        this.managedConnectionFactory = managedConnectionFactory;
        this.cxManager = cxManager;
    }

    @Override
    public MqttConnection getConnection() {
        logger.fine("Asking a connection without credentials");
        MqttConnectionRequestInfo mqttConnectionRequestInfo = new MqttConnectionRequestInfo();
        try {
            return (MqttConnection) this.cxManager.allocateConnection(this.managedConnectionFactory, mqttConnectionRequestInfo);
        } catch (ResourceException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public MqttConnection getConnection(String userName, String password) {
        logger.fine("Asking a connection with login " + userName + " and a password");
        MqttConnectionRequestInfo mqttConnectionRequestInfo = new MqttConnectionRequestInfo();
        mqttConnectionRequestInfo.setUserName(userName);
        mqttConnectionRequestInfo.setPassword(password);
        try {
            return (MqttConnection) this.cxManager.allocateConnection(this.managedConnectionFactory, mqttConnectionRequestInfo);
        } catch (ResourceException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setReference(Reference reference) {
        this.reference = reference;
    }

    @Override
    public Reference getReference() throws NamingException {
        return reference;
    }
}
