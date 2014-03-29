package fr.sewatech.mqttra.connector.inbound;

import org.fusesource.mqtt.client.Callback;

/**
* @author Alexis Hassler
*/
class LoggingCallback<T> implements Callback<T> {
    private String name;

    public LoggingCallback(String name) {
        this.name = name;
    }

    @Override
    public void onSuccess(T value) {
        System.out.println("Success : " + name);
    }

    @Override
    public void onFailure(Throwable value) {
        System.out.println("Failure : " + name);
    }
}
