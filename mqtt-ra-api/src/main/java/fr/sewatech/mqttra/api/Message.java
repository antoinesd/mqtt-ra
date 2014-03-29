package fr.sewatech.mqttra.api;

/**
 * @author Alexis Hassler
 */
public class Message {

    private String topic;
    private byte[] payload;

    public Message(String topic, byte[] payload) {
        this.payload = payload;
        this.topic = topic;
    }

    public byte[] getPayload() {
        return payload;
    }

    public String getTopic() {
        return topic;
    }
}
