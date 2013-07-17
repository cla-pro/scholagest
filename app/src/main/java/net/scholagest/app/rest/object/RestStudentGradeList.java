package net.scholagest.app.rest.object;

import java.util.Map;

public class RestStudentGradeList {
    private Map<String, Map<String, RestObject>> grades;
    private String yearKey;
    private String classKey;
    private String branchKey;
    private String periodKey;

    public Map<String, Map<String, RestObject>> getGrades() {
        return grades;
    }

    public void setGrades(Map<String, Map<String, RestObject>> grades) {
        this.grades = grades;
    }

    public String getYearKey() {
        return yearKey;
    }

    public void setYearKey(String yearKey) {
        this.yearKey = yearKey;
    }

    public String getClassKey() {
        return classKey;
    }

    public void setClassKey(String classKey) {
        this.classKey = classKey;
    }

    public String getBranchKey() {
        return branchKey;
    }

    public void setBranchKey(String branchKey) {
        this.branchKey = branchKey;
    }

    public String getPeriodKey() {
        return periodKey;
    }

    public void setPeriodKey(String periodKey) {
        this.periodKey = periodKey;
    }
}
