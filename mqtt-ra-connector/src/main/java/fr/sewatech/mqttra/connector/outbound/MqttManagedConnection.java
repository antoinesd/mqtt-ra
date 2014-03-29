package fr.sewatech.mqttra.connector.outbound;

import fr.sewatech.mqttra.api.MqttConnection;
import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;

import javax.resource.ResourceException;
import javax.resource.spi.ConnectionEventListener;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.LocalTransaction;
import javax.resource.spi.ManagedConnectionMetaData;
import javax.security.auth.Subject;
import javax.transaction.xa.XAResource;
import java.io.PrintWriter;

public class MqttManagedConnection implements javax.resource.spi.ManagedConnection {

    private BlockingConnection connection;
    private PrintWriter logWriter;
    private MqttConnectionRequestInfo defaultConnectionRequestInfo;

    public MqttManagedConnection(MqttConnectionRequestInfo defaultConnectionRequestInfo) {
        this.defaultConnectionRequestInfo = defaultConnectionRequestInfo;
    }

    @Override
    public MqttConnection getConnection(Subject subject, ConnectionRequestInfo cxRequestInfo) throws ResourceException {
        try {
            MQTT mqtt = new MQTT();
            mqtt.setHost(defaultConnectionRequestInfo.getServerUrl());
            connection = mqtt.blockingConnection();
            connection.connect();
            MqttBlockingConnectionImpl mqttBlockingConnection = new MqttBlockingConnectionImpl(connection);
            mqttBlockingConnection.setDefaultQos(defaultConnectionRequestInfo.getQos());
            mqttBlockingConnection.setDefaultTopic(defaultConnectionRequestInfo.getTopicName());
            return mqttBlockingConnection;
        } catch (Exception e) {
            throw new ResourceException(e);
        }
    }

    @Override
    public void destroy() throws ResourceException {
        try {
            this.connection.kill();
        } catch (Exception e) {
            throw new ResourceException(e);
        } finally {
            this.connection = null;
        }
    }

    @Override
    public void cleanup() throws ResourceException {

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
