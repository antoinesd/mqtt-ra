package fr.sewatech.mqttra.connector.outbound;

import org.fusesource.mqtt.client.BlockingConnection;

import javax.resource.ResourceException;
import javax.resource.spi.ConnectionRequestInfo;
import javax.security.auth.Subject;

/**
 * @author Alexis Hassler
 */
public interface MqttManagedConnection {
    fr.sewatech.mqttra.api.MqttConnection getConnection(Subject subject, ConnectionRequestInfo cxRequestInfo) throws ResourceException;
}
