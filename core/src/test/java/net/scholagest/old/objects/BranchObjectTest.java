package net.scholagest.old.objects;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import net.scholagest.old.database.ITransaction;
import net.scholagest.old.managers.ontology.types.DBSet;
import net.scholagest.old.namespace.CoreNamespace;
import net.scholagest.old.objects.BranchObject;
import net.scholagest.old.objects.BranchType;

import org.junit.Test;
import org.mockito.Mockito;

public class BranchObjectTest {
    @Test
    public void testBranchObject() {
        ITransaction transaction = Mockito.mock(ITransaction.class);

        String branchKey = "branchKey";
        String classKey = "classKey";
        String name = "branchName";
        BranchType type = BranchType.ALPHA_NUMERICAL;
        DBSet periods = new DBSet(transaction, "");
        Map<String, Object> properties = new HashMap<>();
        properties.put(CoreNamespace.pBranchClass, classKey);
        properties.put(CoreNamespace.pBranchType, type.name());
        properties.put(CoreNamespace.pBranchName, name);
        properties.put(CoreNamespace.pBranchPeriods, periods);

        BranchObject branchObject = new BranchObject(branchKey);
        branchObject.setProperties(properties);

        assertEquals(branchKey, branchObject.getKey());
        assertEquals(CoreNamespace.tBranch, branchObject.getType());
        assertEquals(classKey, branchObject.getClassKey());
        assertEquals(type, branchObject.getBranchType());
        assertEquals(periods, branchObject.getPeriods());

        branchObject.setClassKey(classKey);
        assertEquals(classKey, branchObject.getClassKey());

        branchObject.setPeriods(periods);
        assertEquals(periods, branchObject.getPeriods());
    }

    @Test
    public void testDefaultBranchType() {
        assertEquals(BranchType.NUMERICAL, new BranchObject(null).getBranchType());
    }
}
