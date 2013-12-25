package fr.sewatech.mqttra.connector;

import fr.sewatech.mqttra.api.MqttListener;
import org.fusesource.mqtt.client.*;

import javax.resource.ResourceException;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.BootstrapContext;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.ResourceAdapterInternalException;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.transaction.xa.XAResource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Alexis Hassler
 */
public class MqttAdapter implements ResourceAdapter {

    Map<Key, MqttListener> endPoints = new HashMap<Key, MqttListener>();
    Map<Key, CallbackConnection> connections = new HashMap<Key, CallbackConnection>();

    @Override
    public void start(BootstrapContext bootstrapContext) throws ResourceAdapterInternalException {
    }

    @Override
    public void stop() {
    }

    /**
     * Déploiement d'un MDB : branchement d'un client MQTT ?
     *
     * @param mdbFactory
     * @param activationSpec
     * @throws ResourceException
     */
    @Override
    public void endpointActivation(MessageEndpointFactory mdbFactory, ActivationSpec activationSpec) throws ResourceException {
        final ActivationSpecBean spec = (ActivationSpecBean) activationSpec;
        final MqttListener mdb = (MqttListener) mdbFactory.createEndpoint(null);
        Key key = new Key(mdbFactory, activationSpec);

        try {
            MQTT mqtt = new MQTT();
            mqtt.setHost(spec.getServerUrl());
            final CallbackConnection connection = mqtt.callbackConnection();
            connections.put(key, connection);
            endPoints.put(key, mdb);

            connection.listener(new MqttConnectionListener(mdb));

            connection.connect(new SilentCallback<Void>() {
                @Override
                public void onSuccess(Void value) {
                    connection.subscribe(new Topic[]{new Topic(spec.getTopicName(), spec.getQoS())}, new SilentCallback<byte[]>());
                }
            });
        } catch (Exception e) {
            throw new ResourceException(e);
        }

    }

    @Override
    public void endpointDeactivation(MessageEndpointFactory mdbFactory, ActivationSpec activationSpec) {
        Key key = new Key(mdbFactory, activationSpec);
        try {
            CallbackConnection connection = connections.remove(key);
            if (connection != null) {
                connection.disconnect(new SilentCallback<Void>());
            }

            MqttListener listener = endPoints.remove(key);
        } catch (Exception e) {
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
