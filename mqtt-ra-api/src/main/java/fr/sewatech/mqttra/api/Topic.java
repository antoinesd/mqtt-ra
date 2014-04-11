package fr.sewatech.mqttra.api;

import org.fusesource.mqtt.client.QoS;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Alexis Hassler
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Topic {
    public String name();
    public QoS qos();
}
