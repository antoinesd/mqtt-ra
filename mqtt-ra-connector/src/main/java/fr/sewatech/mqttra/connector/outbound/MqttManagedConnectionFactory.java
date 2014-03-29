package fr.sewatech.mqttra.connector.outbound;

import fr.sewatech.mqttra.api.MqttConnectionFactory;
import org.fusesource.mqtt.client.BlockingConnection;

import javax.resource.ResourceException;
import javax.resource.spi.*;
import javax.security.auth.Subject;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Set;

@ConnectionDefinition(connectionFactory = MqttConnectionFactoryImpl.class, connectionFactoryImpl = MqttConnectionFactoryImpl.class,
                      connection = BlockingConnection.class, connectionImpl = BlockingConnection.class)
public class MqttManagedConnectionFactory implements ManagedConnectionFactory, ResourceAdapterAssociation, Serializable {

    private PrintWriter logWriter;
    private ResourceAdapter ra;

    public MqttManagedConnectionFactory() {
        super();
        System.out.println("===================");
    }

    @Override
    public Object createConnectionFactory(ConnectionManager cxManager) throws ResourceException {
        MqttConnectionFactory mqttConnectionFactory = new MqttConnectionFactoryImpl(this, cxManager);
        return mqttConnectionFactory;
    }

    @Override
    public Object createConnectionFactory() throws ResourceException {
        return createConnectionFactory(new MqttConnectionManager());
    }

    @Override
    public ManagedConnection createManagedConnection(Subject subject, ConnectionRequestInfo cxRequestInfo) throws ResourceException {
        return new MqttManagedConnectionImpl();
    }

    @Override
    public ManagedConnection matchManagedConnections(Set connectionSet, Subject subject, ConnectionRequestInfo cxRequestInfo) throws ResourceException {
        throw null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws ResourceException {
        this.logWriter = out;
    }

    @Override
    public PrintWriter getLogWriter() throws ResourceException {
        return logWriter;
    }

    @Override
    public ResourceAdapter getResourceAdapter() {
        return ra;
    }

    @Override
    public void setResourceAdapter(ResourceAdapter ra) throws ResourceException {
        this.ra = ra;
    }
}
