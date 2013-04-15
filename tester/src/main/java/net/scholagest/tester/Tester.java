package net.scholagest.tester;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import net.scholagest.tester.jaxb.TCall;
import net.scholagest.tester.jaxb.TCalls;
import net.scholagest.tester.jaxb.TScenario;

public class Tester {
    public static void main(String[] args) throws JAXBException, IOException {
        new Tester().run();
    }

    public void initialize() {

    }

    public void run() throws JAXBException, IOException {
        TScenario scenario = readScenario();

        new ScenarioPlayer().playScenario(scenario);
    }

    private TScenario readScenario() throws JAXBException, IOException {
        JAXBContext jc = JAXBContext.newInstance(new Class[] { TCall.class, TCalls.class, TScenario.class });

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        InputStream xml = ClassLoader.getSystemResourceAsStream("scenario" + File.separatorChar + "scenario1.xml");
        JAXBElement<TScenario> scenario = unmarshaller.unmarshal(new StreamSource(xml), TScenario.class);
        xml.close();

        return scenario.getValue();
    }
}
