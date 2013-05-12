package net.scholagest.tester;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import net.scholagest.tester.jaxb.TCall;
import net.scholagest.tester.jaxb.TScenario;
import net.scholagest.tester.result.CallResult;
import net.scholagest.tester.result.FieldResult;
import net.scholagest.tester.result.TestResult;
import net.scholagest.tester.result.TestResultStatus;

public class ScenarioPlayer {
    public void playScenarioList(List<TScenario> scenarioList) {
        List<TestResult> testResultList = new ArrayList<>();
        for (TScenario scenario : scenarioList) {
            testResultList.add(playScenario(scenario));
        }

        printResults(testResultList);
    }

    private TestResult playScenario(TScenario scenario) {
        Placeholder placeholder = new Placeholder();
        ResponseAnalyzer responseAnalyzer = new ResponseAnalyzer(placeholder, scenario.getName());

        for (TCall call : scenario.getCalls().getCall()) {
            new CallHandler(responseAnalyzer, placeholder).handleCallAndException(scenario.getBaseURL(), call);
        }

        return responseAnalyzer.getTestResult();
    }

    private TestResultStatus areAllTestResultsOk(List<TestResult> testResultList) {
        for (TestResult testResult : testResultList) {
            if (testResult.getTestStatus() != TestResultStatus.OK) {
                return TestResultStatus.ERROR;
            }
        }

        return TestResultStatus.OK;
    }

    private void printResults(List<TestResult> testResultList) {
        for (TestResult testResult : testResultList) {
            displayResults(testResult);
        }

        System.out.println("End result: " + areAllTestResultsOk(testResultList));
    }

    private void displayResults(TestResult testResult) {
        System.out.println(String.format("========================== Test: %s", testResult.getTestName()));

        for (CallResult result : testResult.getResults()) {
            System.out.println(String.format("-------------- Call id: %s", result.getCall().getId()));
            System.out.println(String.format("Url: %s", result.getCall().getUrl()));
            System.out.println(String.format("Parameters: %s", result.getCall().getParameters()));
            System.out.println(String.format("Result status: %s", result.getStatus()));
            try {
                System.out.println(String.format("Response: %s", result.getContentExchange().getResponseContent()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            for (FieldResult fieldResult : result.getFieldResults()) {
                System.out.println(String.format("%10s | %50s | %50s | %50s", fieldResult.getStatus().name(), fieldResult.getExpectedValue(),
                        fieldResult.getReceivedValue(), fieldResult.getPath()));
            }
        }

        System.out.println(String.format("========================== Result: %s", testResult.getTestStatus().name()));
        System.out.println("==============================================================================");
    }
}
