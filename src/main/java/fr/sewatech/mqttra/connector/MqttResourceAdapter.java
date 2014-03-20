package fr.sewatech.mqttra.connector;

import fr.sewatech.mqttra.api.MqttListener;
import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.hawtdispatch.Task;
import org.fusesource.hawtdispatch.internal.HawtDispatcher;
import org.fusesource.hawtdispatch.internal.SerialDispatchQueue;
import org.fusesource.mqtt.client.*;

import javax.resource.ResourceException;
import javax.resource.spi.*;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.transaction.xa.XAResource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static javax.resource.spi.TransactionSupport.TransactionSupportLevel.NoTransaction;

/**
 * @author Alexis Hassler
 */
@Connector(
        vendorName = "sewatech", version = "0.1", eisType = "MQTT Inbound Adapter",
        transactionSupport = NoTransaction)
public class MqttResourceAdapter implements ResourceAdapter {

    Map<Key, MqttListener> endPoints = new HashMap<>();
    Map<Key, CallbackConnection> connections = new HashMap<>();

    @Override
    public void start(BootstrapContext bootstrapContext) throws ResourceAdapterInternalException {
        System.out.println("MqttResourceAdapter.start");
    }

    @Override
    public void stop() {
        System.out.println("MqttResourceAdapter.stop");
    }

    /**
     * Déploiement d'un MDB : branchement d'un client MQTT
     */
    @Override
    public void endpointActivation(MessageEndpointFactory mdbFactory, ActivationSpec activationSpec) throws ResourceException {
        final ActivationSpecBean spec = ActivationSpecBean.class.cast(activationSpec);
        final MqttListener mdb = MqttListener.class.cast(mdbFactory.createEndpoint(null));
        final Key key = new Key(mdbFactory, activationSpec);

        try {
            MQTT mqtt = new MQTT();
            mqtt.setHost(spec.getServerUrl());
            final CallbackConnection connection = mqtt.callbackConnection();

            connection.listener(new MqttConnectionListener(mdb));

            connection.connect(new LoggingCallback<Void>("connect") {
                @Override
                public void onSuccess(Void value) {
                    super.onSuccess(value);

                    connection.subscribe(spec.buildTopicArray(), new LoggingCallback<byte[]>("subscribe"));
                    connections.put(key, connection);
                    endPoints.put(key, mdb);
                }
            });
        } catch (Exception e) {
            throw new ResourceException(e);
        }
    }

    @Override
    public void endpointDeactivation(MessageEndpointFactory mdbFactory, ActivationSpec activationSpec) {
        final ActivationSpecBean spec = ActivationSpecBean.class.cast(activationSpec);
        System.out.println("MqttResourceAdapter.endpointDeactivation for " + spec.getTopicName() );
        Key key = new Key(mdbFactory, spec);
        try {
            endPoints.remove(key);
            final CallbackConnection connection = connections.remove(key);
            if (connection != null) {
                connection.suspend();  // in order to skip other messages in the topic
                connection.getDispatchQueue().execute(new Task() {
                    @Override
                    public void run() {
                        connection.kill(new LoggingCallback<Void>("====> disconnect"));
                    }
                });
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public XAResource[] getXAResources(ActivationSpec[] activationSpecs) throws ResourceException {
        return new XAResource[0];
    }

    class Key {
        private MessageEndpointFactory factory;
        private ActivationSpec activationSpec;

        Key(MessageEndpointFactory factory, ActivationSpec activationSpec) {
            this.factory = factory;
            this.activationSpec = activationSpec;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Key that = (Key) o;

            return Objects.equals(this.factory, that.factory)
                    && Objects.equals(this.activationSpec, that.activationSpec);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.factory, this.activationSpec);
        }
    }
}
