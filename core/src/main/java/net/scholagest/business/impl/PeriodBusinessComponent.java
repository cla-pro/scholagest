package net.scholagest.business.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.business.IPeriodBusinessComponent;
import net.scholagest.managers.IClassManager;
import net.scholagest.managers.IPeriodManager;
import net.scholagest.managers.IStudentManager;
import net.scholagest.objects.BaseObject;
import net.scholagest.objects.ClassObject;
import net.scholagest.objects.PeriodObject;

import com.google.inject.Inject;

public class PeriodBusinessComponent implements IPeriodBusinessComponent {
    private IPeriodManager periodManager;
    private IClassManager classManager;
    private IStudentManager studentManager;

    @Inject
    public PeriodBusinessComponent(IPeriodManager periodManager, IClassManager classManager, IStudentManager studentManager) {
        this.periodManager = periodManager;
        this.classManager = classManager;
        this.studentManager = studentManager;
    }

    @Override
    public void setPeriodProperties(String periodKey, Map<String, Object> periodProperties) throws Exception {
        periodManager.setPeriodProperties(periodKey, periodProperties);
    }

    @Override
    public PeriodObject getPeriodProperties(String periodKey, Set<String> properties) throws Exception {
        return periodManager.getPeriodProperties(periodKey, properties);
    }
    
    @Override
    public Map<String, Map<String, BaseObject>> getPeriodMeans(String periodKey, Set<String> studentKeys) {
    	PeriodObject periodObject = periodManager.getPeriodProperties(periodKey, new HashSet<String>());
    	String meanExamKey = periodObject.getMeanKey();
    	
    	ClassObject classObject = classManager.getClassProperties(periodObject.getClassKey(), new HashSet<String>());
    	String yearKey = classObject.getYearKey();
    	
    	Map<String, BaseObject> grades = new HashMap<>();
    	for (String studentKey : studentKeys) {
    		BaseObject gradeObject = studentManager.getStudentGrades(studentKey, new HashSet<String>(Arrays.asList(meanExamKey)), yearKey).get(meanExamKey);
    		grades.put(studentKey, gradeObject);
    	}
    	
    	Map<String, Map<String, BaseObject>> means = new HashMap<>();
    	means.put(meanExamKey, grades);
    	return means;
    }
}
