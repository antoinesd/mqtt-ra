package fr.sewatech.mqttra.connector.outbound;

import org.fusesource.mqtt.client.BlockingConnection;

import javax.resource.ResourceException;
import javax.resource.spi.*;
import javax.security.auth.Subject;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Set;
import java.util.logging.Logger;

@ConnectionDefinition(connectionFactory = MqttConnectionFactoryImpl.class, connectionFactoryImpl = MqttConnectionFactoryImpl.class,
                      connection = BlockingConnection.class, connectionImpl = BlockingConnection.class)
public class MqttManagedConnectionFactory implements ManagedConnectionFactory, ResourceAdapterAssociation, Serializable {

    private static final Logger logger = Logger.getLogger(MqttManagedConnectionFactory.class.getName());

    private PrintWriter logWriter;
    private ResourceAdapter ra;

    private MqttConnectionRequestInfo defaultConnectionRequestInfo = new MqttConnectionRequestInfo();

    public MqttManagedConnectionFactory() {
        super();
    }

    @Override
    public Object createConnectionFactory(ConnectionManager cxManager) throws ResourceException {
        return new MqttConnectionFactoryImpl(this, cxManager);
    }

    @Override
    public Object createConnectionFactory() throws ResourceException {
        return createConnectionFactory(new MqttConnectionManager());
    }

    @Override
    public ManagedConnection createManagedConnection(Subject subject, ConnectionRequestInfo cxRequestInfo) throws ResourceException {
        logger.fine("Creating managed connection");
        return new MqttManagedConnection(( (MqttConnectionRequestInfo) cxRequestInfo).mergeWith(defaultConnectionRequestInfo) );
    }

    @Override
    public ManagedConnection matchManagedConnections(Set connectionSet, Subject subject, ConnectionRequestInfo cxRequestInfo) throws ResourceException {
        logger.fine("Trying to match a managed connection");
        return null;
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

    public void setServerUrl(String serverUrl) {
        this.defaultConnectionRequestInfo.setServerUrl(serverUrl);
    }

    public void setDefaultQosLevel(int qosLevel) {
        this.defaultConnectionRequestInfo.setQosLevel(qosLevel);
    }

    public void setDefaultTopic(String defaultTopic) {
        this.defaultConnectionRequestInfo.setTopicName(defaultTopic);
    }

    public void setUserName(String userName) {
        this.defaultConnectionRequestInfo.setUserName(userName);
    }

    public void setPassword(String password) {
        this.defaultConnectionRequestInfo.setPassword(password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MqttManagedConnectionFactory that = (MqttManagedConnectionFactory) o;

        if (!defaultConnectionRequestInfo.equals(that.defaultConnectionRequestInfo)) return false;
        if (!ra.equals(that.ra)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = ra.hashCode();
        result = 31 * result + defaultConnectionRequestInfo.hashCode();
        return result;
    }
}
