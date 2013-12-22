package fr.sewatech.mqttra;

import fr.sewatech.mqttra.api.MqttListener;
import fr.sewatech.mqttra.connector.MqttAdapter;
import fr.sewatech.mqttra.example.MqttBean;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

@RunWith(Arquillian.class)
public class Runner {

    @Deployment(testable = false)
    public static EnterpriseArchive createDeployment() {

        final JavaArchive rarLib = ShrinkWrap.create(JavaArchive.class, "connector.jar");
        rarLib.addPackages(true, MqttAdapter.class.getPackage());
        rarLib.addPackages(true, MqttListener.class.getPackage());
        System.out.println(rarLib.toString(true));
        System.out.println();

        final EnterpriseArchive rar = ShrinkWrap.create(EnterpriseArchive.class, "connectorx.rar");
//        rar.addAsModule(rarLib);
        rar.addAsManifestResource(new ClassLoaderAsset("META-INF/ra.xml"), "ra.xml");
        System.out.println(rar.toString(true));
        System.out.println();

        final JavaArchive jar = ShrinkWrap.create(JavaArchive.class, "mdb.jar");
        jar.addPackages(true, MqttBean.class.getPackage());
        System.out.println(jar.toString(true));
        System.out.println();

        // Make the EAR
        final EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "test.ear")
                .addAsLibrary(rarLib)
                .addAsModule(rar)
                .addAsModule(jar);
        System.out.println(ear.toString(true));
        System.out.println();

        return ear;
    }

    @Test
    public void run(){
        try {
            Thread.sleep(TimeUnit.HOURS.toMillis(1));
        } catch (InterruptedException e) {
            Thread.interrupted();
        }
    }
}