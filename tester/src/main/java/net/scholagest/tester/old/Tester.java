package net.scholagest.tester.old;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import net.scholagest.tester.jaxb.TCall;
import net.scholagest.tester.jaxb.TCalls;
import net.scholagest.tester.jaxb.TExpectedResults;
import net.scholagest.tester.jaxb.TResult;
import net.scholagest.tester.jaxb.TScenario;

public class Tester {
    private static final String SCENARIO_FOLDER = "scenario";

    private List<String> filenameList = new ArrayList<>();

    public static void main(String[] args) throws JAXBException, IOException {
        new Tester().initialize(args).run();
    }

    public Tester initialize(String[] args) {
        filenameList = findAllScenario();
        return this;
    }

    private List<String> findAllScenario() {
        List<String> filenames = new ArrayList<>();

        try {
            List<String> allResources = getResourceListing(SCENARIO_FOLDER);
            List<String> scenarioFileNameList = filterResourceList(allResources);
            filenames.addAll(scenarioFileNameList);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        System.out.println(filenames);
        return filenames;
    }

    private List<String> getResourceListing(String folderPath) throws URISyntaxException, UnsupportedEncodingException, IOException {
        URL folderURL = ClassLoader.getSystemResource(SCENARIO_FOLDER);
        if (folderURL != null && folderURL.getProtocol().equals("file")) {
            String[] fileNameList = new File(folderURL.toURI()).list();
            List<String> resourceListing = new ArrayList<>();
            for (String fileName : fileNameList) {
                resourceListing.add(SCENARIO_FOLDER + File.separatorChar + fileName);
            }
            return resourceListing;
        }

        if (folderURL == null) {
            String me = getClass().getName().replace(".", "/") + ".class";
            folderURL = getClass().getClassLoader().getResource(me);
        }

        Set<String> result = new HashSet<String>();

        if (folderURL.getProtocol().equals("jar")) {
            String jarPath = folderURL.getPath().substring(5, folderURL.getPath().indexOf("!"));
            JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                String name = entries.nextElement().getName();
                if (name.startsWith(folderPath)) {
                    String entry = name.substring(folderPath.length());
                    int checkSubdir = entry.indexOf("/");
                    if (checkSubdir >= 0) {
                        // if it is a subdirectory, we just return the directory
                        // name
                        entry = entry.substring(0, checkSubdir);
                    }
                    result.add(entry);
                }
            }
        }

        return new ArrayList<>(result);
    }

    private List<String> filterResourceList(List<String> allResources) {

        return allResources;
    }

    public void run() throws JAXBException, IOException {
        List<TScenario> scenarioList = readScenarioList(filenameList);

        // readScenario(SCENARIO_FOLDER + File.separatorChar +
        // "006_authenticate_year_fail.xml"));
        new ScenarioPlayer().playScenarioList(scenarioList);
    }

    private List<TScenario> readScenarioList(List<String> filenameList) throws JAXBException, IOException {
        List<TScenario> scenarioList = new ArrayList<>();

        for (String filename : filenameList) {
            System.out.println("Reading scenario: " + filename);
            scenarioList.add(readScenario(filename));
        }

        return scenarioList;
    }

    private TScenario readScenario(String filename) throws JAXBException, IOException {
        JAXBContext jc = JAXBContext.newInstance(new Class[] { TCall.class, TCalls.class, TScenario.class, TResult.class, TExpectedResults.class });

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        InputStream xml = ClassLoader.getSystemResourceAsStream(filename);
        JAXBElement<TScenario> scenario = unmarshaller.unmarshal(new StreamSource(xml), TScenario.class);
        xml.close();

        return scenario.getValue();
    }
}
