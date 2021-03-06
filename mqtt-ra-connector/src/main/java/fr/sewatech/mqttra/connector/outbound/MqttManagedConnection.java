package fr.sewatech.mqttra.connector.outbound;

import fr.sewatech.mqttra.api.MqttConnection;
import org.fusesource.mqtt.client.BlockingConnection;

import javax.resource.ResourceException;
import javax.resource.spi.ConnectionEventListener;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.LocalTransaction;
import javax.resource.spi.ManagedConnectionMetaData;
import javax.security.auth.Subject;
import javax.transaction.xa.XAResource;
import java.io.PrintWriter;
import java.util.logging.Logger;

public class MqttManagedConnection implements javax.resource.spi.ManagedConnection {

    private static final Logger logger = Logger.getLogger(MqttManagedConnectionFactory.class.getName());

    private BlockingConnection connection;
    private PrintWriter logWriter;
    private MqttConnectionRequestInfo defaultConnectionRequestInfo;

    public MqttManagedConnection(MqttConnectionRequestInfo connectionRequestInfo) {
        this.defaultConnectionRequestInfo = connectionRequestInfo;
    }

    @Override
    public MqttConnection getConnection(Subject subject, ConnectionRequestInfo cxRequestInfo) throws ResourceException {
        logger.fine("Getting connection");
        return new MqttBlockingConnectionImpl( ((MqttConnectionRequestInfo) cxRequestInfo).mergeWith(defaultConnectionRequestInfo) );
    }

    @Override
    public void destroy() throws ResourceException {
        try {
            logger.fine("Destroying");
            this.connection.kill();
        } catch (Exception e) {
            throw new ResourceException(e);
        } finally {
            this.connection = null;
        }
    }

    @Override
    public void cleanup() throws ResourceException {
        logger.fine("Would like to cleanup, but nothing done");
    }

    @Override
    public void associateConnection(Object connection) throws ResourceException {
        this.connection = (BlockingConnection) connection;
    }

    @Override
    public void addConnectionEventListener(ConnectionEventListener listener) {

    }

    @Override
    public void removeConnectionEventListener(ConnectionEventListener listener) {

    }

    @Override
    public XAResource getXAResource() throws ResourceException {
        return null;
    }

    @Override
    public LocalTransaction getLocalTransaction() throws ResourceException {
        return new MqttLocalTransaction();
    }

    @Override
    public ManagedConnectionMetaData getMetaData() throws ResourceException {
        return new MqttManagedConnectionMetaData();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws ResourceException {
        this.logWriter = out;
    }

    @Override
    public PrintWriter getLogWriter() throws ResourceException {
        return logWriter;
    }
}
