package net.scholagest.tester.result;

import java.util.ArrayList;
import java.util.List;

public class TestResult {
    private String testName;
    private List<CallResult> results = new ArrayList<>();

    public TestResult(String testName) {
        this.testName = testName;
    }

    public String getTestName() {
        return testName;
    }

    public void addCallResult(CallResult callResult) {
        results.add(callResult);
    }

    public List<CallResult> getResults() {
        return results;
    }

    public TestResultStatus getTestStatus() {
        for (CallResult callResult : results) {
            if (callResult.getStatus() != CallResultStatus.OK) {
                return TestResultStatus.ERROR;
            }
        }

        return TestResultStatus.OK;
    }
}
