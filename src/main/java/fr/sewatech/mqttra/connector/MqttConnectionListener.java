package fr.sewatech.mqttra.connector;

import fr.sewatech.mqttra.api.Message;
import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Listener;

/**
* @author Alexis Hassler
*/
class MqttConnectionListener implements Listener {
    private final fr.sewatech.mqttra.api.MqttListener mdb;

    public MqttConnectionListener(fr.sewatech.mqttra.api.MqttListener mdb) {
        this.mdb = mdb;
    }

    @Override
    public void onConnected() {
    }

    @Override
    public void onDisconnected() {
    }

    @Override
    public void onPublish(UTF8Buffer topic, Buffer message, Runnable ack) {
        mdb.onMessage(new Message(topic.toString(), message.toByteArray()));
        ack.run();
    }

    @Override
    public void onFailure(Throwable value) {
    }
}
