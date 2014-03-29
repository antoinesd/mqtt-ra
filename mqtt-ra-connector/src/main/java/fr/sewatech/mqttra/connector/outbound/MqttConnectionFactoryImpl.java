package fr.sewatech.mqttra.connector.outbound;

import fr.sewatech.mqttra.api.MqttConnection;
import fr.sewatech.mqttra.api.MqttConnectionFactory;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.resource.Referenceable;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ManagedConnectionFactory;

/**
 * @author Alexis Hassler
 */
public class MqttConnectionFactoryImpl implements Referenceable, MqttConnectionFactory {
    private final ManagedConnectionFactory managedConnectionFactory;
    private final ConnectionManager cxManager;
    private Reference reference;

    public MqttConnectionFactoryImpl(ManagedConnectionFactory managedConnectionFactory, ConnectionManager cxManager) {
        System.out.println("===> new MqttConnectionFactoryImpl with MCF : " + managedConnectionFactory);
        this.managedConnectionFactory = managedConnectionFactory;
        this.cxManager = cxManager;
    }

    @Override
    public MqttConnection getConnection() {
        try {
            return (MqttConnection) this.cxManager.allocateConnection(this.managedConnectionFactory, null);
        } catch (ResourceException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setReference(Reference reference) {
        this.reference = reference;
        System.out.println("===> Reference for MqttConnectionFactoryImpl = " + reference);
    }

    @Override
    public Reference getReference() throws NamingException {
        return reference;
    }
}
