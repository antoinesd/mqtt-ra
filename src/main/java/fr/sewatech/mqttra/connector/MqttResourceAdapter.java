package fr.sewatech.mqttra.connector;

import fr.sewatech.mqttra.api.Message;
import fr.sewatech.mqttra.api.MqttListener;
import org.fusesource.hawtdispatch.Task;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.MQTT;

import javax.resource.ResourceException;
import javax.resource.spi.*;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkException;
import javax.transaction.xa.XAResource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static java.lang.reflect.Proxy.newProxyInstance;
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
    private BootstrapContext bootstrapContext;

    @Override
    public void start(BootstrapContext bootstrapContext) throws ResourceAdapterInternalException {
        System.out.println("MqttResourceAdapter.start");
        this.bootstrapContext = bootstrapContext;
    }

    @Override
    public void stop() {
        System.out.println("MqttResourceAdapter.stop");
    }

    /**
     * Déploiement d'un MDB : branchement d'un client MQTT
     */
    @Override
    public void endpointActivation(final MessageEndpointFactory mdbFactory, ActivationSpec activationSpec) throws ResourceException {
        final ActivationSpecBean spec = ActivationSpecBean.class.cast(activationSpec);
        final Key key = new Key(mdbFactory, activationSpec);

        try {
            MQTT mqtt = new MQTT();
            mqtt.setHost(spec.getServerUrl());
            final CallbackConnection connection = mqtt.callbackConnection();

            int poolSize = spec.getPoolSize();
            final BlockingQueue<MqttListener> pool = new ArrayBlockingQueue<>(poolSize);
            for (int i = 0; i < poolSize; i++) {
                pool.add(MqttListener.class.cast(mdbFactory.createEndpoint(null)));
            }


            System.out.println("Pool size : " + pool.size());
            final MqttListener mdb = new MqttListener() {
                @Override
                public void onMessage(final Message message) {
                    final MqttListener listener;
                    try {
                        listener = pool.take();

                        bootstrapContext.getWorkManager().startWork(new Work() {
                            @Override
                            public void run() {
                                try {
                                    listener.onMessage(message);
                                    pool.add(listener);
                                } catch (Throwable e) {
                                    throw new RuntimeException(e);
                                }
                            }

                            @Override
                            public void release() {
                            }
                        });
                    } catch (WorkException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

            };
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
