package fr.sewatech.mqttra;

import fr.sewatech.mqttra.api.MqttListener;
import fr.sewatech.mqttra.connector.MqttAdapter;
import fr.sewatech.mqttra.example.FirstMqttBean;
import fr.sewatech.mqttra.example.Messages;
import fr.sewatech.mqttra.example.SecondMqttBean;
import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class MqttTest {

    @Deployment
    public static EnterpriseArchive createDeployment() {

        JavaArchive rarLib = ShrinkWrap.create(JavaArchive.class, "connector.jar")
                .addPackages(true, MqttAdapter.class.getPackage())
                .addPackages(true, MqttListener.class.getPackage());

        EnterpriseArchive rar = ShrinkWrap.create(EnterpriseArchive.class, "connector.rar")
                .addAsManifestResource(new ClassLoaderAsset("META-INF/ra.xml"), "ra.xml")
                .addAsLibrary(rarLib);

        JavaArchive jarLib = ShrinkWrap.create(JavaArchive.class, "lib.jar")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addClasses(Messages.class);

        JavaArchive ejbJar = ShrinkWrap.create(JavaArchive.class, "ejb.jar")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addClasses(FirstMqttBean.class, SecondMqttBean.class);

        WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war")
                .addClasses(MqttTest.class);

        return ShrinkWrap.create(EnterpriseArchive.class, "test.ear")
                .addAsLibrary(rarLib)
                .addAsLibrary(jarLib)
                .addAsModule(ejbJar)
                .addAsModule(rar)
                .addAsModule(war);
    }

    @Test
    public void run() throws Exception {
        MQTT mqtt = new MQTT();
        mqtt.setHost("tcp://localhost:1883");
        BlockingConnection connection = mqtt.blockingConnection();
        connection.connect();
        Thread.sleep(100);
        connection.publish("swt1", "TEST".getBytes(), QoS.AT_MOST_ONCE, false);
        Thread.sleep(100);

        assertEquals(1, Messages.size());
    }
}