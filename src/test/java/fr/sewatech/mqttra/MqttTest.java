package fr.sewatech.mqttra;

import fr.sewatech.mqttra.api.MqttListener;
import fr.sewatech.mqttra.connector.MqttResourceAdapter;
import fr.sewatech.mqttra.example.BlockingMqttBean;
import fr.sewatech.mqttra.example.FirstMqttBean;
import fr.sewatech.mqttra.example.Messages;
import fr.sewatech.mqttra.example.SecondMqttBean;
import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.ResourceAdapterArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class MqttTest {

    @Deployment
    public static EnterpriseArchive createDeployment() {

        JavaArchive raImplLib = ShrinkWrap.create(JavaArchive.class, "connector-impl.jar")
                .addPackages(true, MqttResourceAdapter.class.getPackage());
        JavaArchive raApiLib = ShrinkWrap.create(JavaArchive.class, "connector-api.jar")
                .addPackages(true, MqttListener.class.getPackage());

        ResourceAdapterArchive rar = ShrinkWrap.create(ResourceAdapterArchive.class, new Random().nextInt() + "-connector.rar")
                .addAsLibraries(raImplLib);

        JavaArchive jarLib = ShrinkWrap.create(JavaArchive.class, "lib.jar")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addClasses(Messages.class);

        JavaArchive ejbJar = ShrinkWrap.create(JavaArchive.class, "ejb.jar")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addClasses(FirstMqttBean.class, SecondMqttBean.class)
                .addClasses(BlockingMqttBean.class);

        WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war")
                .addClasses(MqttTest.class);

        return ShrinkWrap.create(EnterpriseArchive.class, "test.ear")
                .addAsLibrary(raApiLib)
                .addAsLibrary(jarLib)
                .addAsModule(rar)
                .addAsModule(ejbJar)
                .addAsModule(war);
    }

    @Test
    public void run() throws Exception {
        MQTT mqtt = new MQTT();
        mqtt.setHost("tcp://localhost:1883");
        BlockingConnection connection = mqtt.blockingConnection();
        connection.connect();
        Thread.sleep(100);
        connection.publish("swt1", "TEST1".getBytes(), QoS.AT_MOST_ONCE, true);
        connection.publish("swt1", "TEST2".getBytes(), QoS.AT_MOST_ONCE, true);
        connection.publish("swt1", "TEST3".getBytes(), QoS.AT_MOST_ONCE, true);
        connection.publish("swt1", "TEST4".getBytes(), QoS.AT_MOST_ONCE, true);
        Thread.sleep(10000);

        assertEquals(3, Messages.size());
    }
}