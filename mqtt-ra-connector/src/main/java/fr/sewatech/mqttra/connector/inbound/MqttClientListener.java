package fr.sewatech.mqttra.connector.inbound;

import fr.sewatech.mqttra.api.Message;
import fr.sewatech.mqttra.api.MqttMessageListener;
import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Listener;

/**
* @author Alexis Hassler
*/
class MqttClientListener implements Listener {
    private final MqttMessageListener mdb;

    public MqttClientListener(MqttMessageListener mdb) {
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
