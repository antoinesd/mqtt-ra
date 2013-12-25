package fr.sewatech.mqttra.api;

import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;

public class Message {

    private UTF8Buffer topic;
    private Buffer payload;

    public Message(UTF8Buffer topic, Buffer payload) {
        this.payload = payload;
        this.topic = topic;
    }

    public byte[] getPayload() {
        return payload.toByteArray();
    }

    public String getTopic() {
        return topic.toString();
    }
}
