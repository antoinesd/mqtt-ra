package fr.sewatech.mqttra.example;

import fr.sewatech.mqttra.api.Message;

import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author Alexis Hassler
 */
public class Messages {
    private static Collection<Message> queue = new ArrayBlockingQueue<>(10, true);

    public static void add(Message message) {
        queue.add(message);
    }

    public static int size () {
        return queue.size();
    }
}
