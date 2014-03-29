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
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import java.io.PrintWriter;

public class MqttManagedConnectionImpl implements javax.resource.spi.ManagedConnection, MqttManagedConnection {

    private BlockingConnection connection;
    private PrintWriter logWriter;

    @Override
    public MqttConnection getConnection(Subject subject, ConnectionRequestInfo cxRequestInfo) throws ResourceException {
        try {
            MQTT mqtt = new MQTT();
            connection = mqtt.blockingConnection();
            connection.connect();
            return new MqttBlockingConnectionImpl(connection);
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
        return new XAResource() {
            @Override
            public void commit(Xid xid, boolean b) throws XAException {

            }

            @Override
            public void end(Xid xid, int i) throws XAException {

            }

            @Override
            public void forget(Xid xid) throws XAException {

            }

            @Override
            public int getTransactionTimeout() throws XAException {
                return 0;
            }

            @Override
            public boolean isSameRM(XAResource xaResource) throws XAException {
                return false;
            }

            @Override
            public int prepare(Xid xid) throws XAException {
                return 0;
            }

            @Override
            public Xid[] recover(int i) throws XAException {
                return new Xid[0];
            }

            @Override
            public void rollback(Xid xid) throws XAException {

            }

            @Override
            public boolean setTransactionTimeout(int i) throws XAException {
                return false;
            }

            @Override
            public void start(Xid xid, int i) throws XAException {

            }
        };
    }

    @Override
    public LocalTransaction getLocalTransaction() throws ResourceException {
        return new LocalTransaction() {
            @Override
            public void begin() throws ResourceException {

            }

            @Override
            public void commit() throws ResourceException {

            }

            @Override
            public void rollback() throws ResourceException {

            }
        };
    }

    @Override
    public ManagedConnectionMetaData getMetaData() throws ResourceException {
        return new ManagedConnectionMetaData() {

            @Override
            public String getEISProductName() throws ResourceException {
                return "??";
            }

            @Override
            public String getEISProductVersion() throws ResourceException {
                return "0.1";
            }

            @Override
            public int getMaxConnections() throws ResourceException {
                return 10;
            }

            @Override
            public String getUserName() throws ResourceException {
                return "Alexis";
            }
        }
                ;
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
