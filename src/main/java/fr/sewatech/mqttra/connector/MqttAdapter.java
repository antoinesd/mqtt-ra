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

/**
 * @author Alexis Hassler
 */
public class MqttAdapter implements ResourceAdapter {

    private BlockingConnection connection;

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
        ActivationSpecBean spec = (ActivationSpecBean) activationSpec;
        MqttListener mdb = (MqttListener) mdbFactory.createEndpoint(null);

        try {
            MQTT mqtt = new MQTT();
            mqtt.setHost(spec.getServerUrl());
            mqtt.setClientId("BlockingConsumer");
            connection = mqtt.blockingConnection();
            connection.connect();

            connection.subscribe(new Topic[]{new Topic(spec.getTopicName(), spec.getQoS())});

            while (true) {
                Message message = connection.receive();
                System.out.println("Hey, message arrived on topic " + message.getTopic() + " : " + new String(message.getPayload(), "UTF-8"));
                mdb.onMessage(message);
                message.ack();
            }
        } catch (Exception e) {
            throw new ResourceException(e);
        }

    }

    @Override
    public void endpointDeactivation(MessageEndpointFactory messageEndpointFactory, ActivationSpec activationSpec) {
        try {
            if (connection != null) {
                connection.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Does nothing as MQTT is not transactional
     * @param activationSpecs
     * @return an empty array
     * @throws ResourceException
     */
    @Override
    public XAResource[] getXAResources(ActivationSpec[] activationSpecs) throws ResourceException {
        return new XAResource[0];
    }
}
