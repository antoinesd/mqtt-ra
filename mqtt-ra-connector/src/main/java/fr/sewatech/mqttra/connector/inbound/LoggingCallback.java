package fr.sewatech.mqttra.connector.inbound;

import org.fusesource.mqtt.client.Callback;

import java.util.logging.Logger;

/**
* @author Alexis Hassler
*/
class LoggingCallback<T> implements Callback<T> {
    private static final Logger logger = Logger.getLogger(LoggingCallback.class.getName());
    private String name;

    public LoggingCallback(String name) {
        this.name = name;
    }

    @Override
    public void onSuccess(T value) {
        logger.fine(name + " : success");
    }

    @Override
    public void onFailure(Throwable value) {
        logger.fine(name + " : failure");
    }
}
