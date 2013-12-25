package fr.sewatech.mqttra.connector;

import org.fusesource.mqtt.client.Callback;

/**
* @author Alexis Hassler
*/
class SilentCallback<T> implements Callback<T> {
    @Override
    public void onSuccess(T value) {
    }

    @Override
    public void onFailure(Throwable value) {
    }
}
