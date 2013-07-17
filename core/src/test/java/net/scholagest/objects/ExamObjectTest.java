package net.scholagest.objects;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import net.scholagest.database.ITransaction;
import net.scholagest.managers.ontology.types.DBSet;
import net.scholagest.namespace.CoreNamespace;

import org.junit.Test;
import org.mockito.Mockito;

public class ExamObjectTest {
    @Test
    public void testExamObject() {
        ITransaction transaction = Mockito.mock(ITransaction.class);

        String examKey = "examKey";
        String classKey = "classKey";
        String name = "examName";
        Integer coeff = 3;
        DBSet grades = new DBSet(transaction, "");
        Map<String, Object> properties = new HashMap<>();
        properties.put(CoreNamespace.pExamClass, classKey);
        properties.put(CoreNamespace.pExamCoeff, "" + coeff);
        properties.put(CoreNamespace.pExamName, name);
        properties.put(CoreNamespace.pExamGrades, grades);

        ExamObject examObject = new ExamObject(examKey);
        examObject.setProperties(properties);

        assertEquals(examKey, examObject.getKey());
        assertEquals(CoreNamespace.tExam, examObject.getType());
        assertEquals(classKey, examObject.getClassKey());
        assertEquals(coeff.intValue(), examObject.getCoeff());
        assertEquals(grades, examObject.getGrades());

        examObject.setClassKey(classKey);
        assertEquals(classKey, examObject.getClassKey());

        examObject.setGrades(grades);
        assertEquals(grades, examObject.getGrades());

        examObject.setCoeff(coeff + 1);
        assertEquals(coeff + 1, examObject.getCoeff());
    }

    @Test
    public void testDefaultCoeff() {
        assertEquals(1, new ExamObject(null).getCoeff());
    }

}
