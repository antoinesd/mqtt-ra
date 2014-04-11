package fr.sewatech.mqttra.connector.inbound;

import fr.sewatech.mqttra.api.Message;
import fr.sewatech.mqttra.api.MqttMessageListener;

import javax.resource.spi.BootstrapContext;
import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkException;
import java.lang.reflect.Method;
import java.util.concurrent.BlockingQueue;

/**
 * @author Alexis Hassler
 */
public class MqttMessageListenerProxy implements MqttMessageListener{

    private BlockingQueue<MqttMessageListener> pool;
    private BootstrapContext bootstrapContext;

    public MqttMessageListenerProxy(BootstrapContext bootstrapContext, BlockingQueue<MqttMessageListener> pool) {
        this.bootstrapContext = bootstrapContext;
        this.pool = pool;
    }

    public void onMessage(final Message message, final Method method) {
        final MqttMessageListener listener;
        try {
            listener = pool.take();

            bootstrapContext.getWorkManager().startWork(new Work() {
                @Override
                public void run() {
                    try {
                        method.invoke(listener, message);
                        pool.add(listener);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void release() {
                }
            });
        } catch (WorkException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
