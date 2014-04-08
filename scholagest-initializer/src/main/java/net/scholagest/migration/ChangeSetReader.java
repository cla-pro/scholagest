package net.scholagest.migration;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import net.scholagest.initializer.jaxb.TChangeProperty;
import net.scholagest.initializer.jaxb.TChangeSet;
import net.scholagest.initializer.jaxb.TCreateProperty;
import net.scholagest.initializer.jaxb.TCreateType;
import net.scholagest.initializer.jaxb.TDbUpdate;
import net.scholagest.initializer.jaxb.TGlobalChangeSets;
import net.scholagest.initializer.jaxb.TRemoveProperty;
import net.scholagest.initializer.jaxb.TRemoveType;
import net.scholagest.initializer.jaxb.TRenameProperty;
import net.scholagest.initializer.jaxb.TRenameType;
import net.scholagest.initializer.jaxb.TSingleChangeSet;

public class ChangeSetReader {
    public List<TChangeSet> getChangeSetListForGlobalChangeSetFile(String globalChangeSetFilename) {
        try {
            TGlobalChangeSets globalChangeSets = readGlobalChangeSet(globalChangeSetFilename);
            List<TChangeSet> changeSetList = new ArrayList<>();

            for (TSingleChangeSet singleChangeSet : globalChangeSets.getChangeset()) {
                String singleChangeSetFilename = singleChangeSet.getFilename();
                changeSetList.add(readSingleChangeSet(singleChangeSetFilename));
            }

            return changeSetList;
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    private TGlobalChangeSets readGlobalChangeSet(String filename) throws JAXBException, IOException {
        JAXBContext jc = JAXBContext.newInstance(new Class[] { TGlobalChangeSets.class, TSingleChangeSet.class });

        return parseXmlAsJaxb(filename, jc, TGlobalChangeSets.class);
    }

    private TChangeSet readSingleChangeSet(String filename) throws JAXBException, IOException {
        JAXBContext jc = JAXBContext.newInstance(new Class[] { TChangeSet.class, TChangeProperty.class, TCreateProperty.class, TCreateType.class,
                TDbUpdate.class, TRemoveProperty.class, TRemoveType.class, TRenameProperty.class, TRenameType.class });

        return parseXmlAsJaxb(filename, jc, TChangeSet.class);
    }

    private <T> T parseXmlAsJaxb(String filename, JAXBContext jc, Class<T> jaxbClass) throws JAXBException, IOException {
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        InputStream xml = ClassLoader.getSystemResourceAsStream(filename);
        JAXBElement<T> unmarschalled = unmarshaller.unmarshal(new StreamSource(xml), jaxbClass);
        xml.close();

        return unmarschalled.getValue();
    }
}
