package net.scholagest.tester.old;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import net.scholagest.initializer.TesterInitializer;
import net.scholagest.tester.jaxb.TCall;
import net.scholagest.tester.jaxb.TScenario;
import net.scholagest.tester.old.result.CallResult;
import net.scholagest.tester.old.result.FieldResult;
import net.scholagest.tester.old.result.TestResult;
import net.scholagest.tester.old.result.TestResultStatus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ScenarioPlayer {
    private static Logger LOG = LogManager.getLogger(ScenarioPlayer.class.getName());
    private static Logger LOG_RESULT = LogManager.getLogger("RESULT");

    public void playScenarioList(List<TScenario> scenarioList) {
        List<TestResult> testResultList = new ArrayList<>();
        for (TScenario scenario : scenarioList) {
            LOG.info("============================= Playing scenario: " + scenario.getName());
            testResultList.add(playScenario(scenario));
            LOG.info("============================= Scenario complete: " + scenario.getName());
        }

        printResults(testResultList);
    }

    private TestResult playScenario(TScenario scenario) {
        Placeholder placeholder = new Placeholder();
        ResponseAnalyzer responseAnalyzer = new ResponseAnalyzer(placeholder, scenario.getName());
        initializeSzenario(scenario.getInitializationFolder());

        for (TCall call : scenario.getCalls().getCall()) {
            LOG.info("Handle call: " + call.getId());
            new CallHandler(responseAnalyzer, placeholder).handleCallAndException(scenario.getBaseURL(), call);
        }

        return responseAnalyzer.getTestResult();
    }

    private void initializeSzenario(String initializationFolder) {
        LOG.info("Reinitializing the DB");
        try {
            TesterInitializer.main(new String[] { initializationFolder });
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        LOG_RESULT.info("=======================================");
        LOG_RESULT.info("==================== All results");

        for (TestResult testResult : testResultList) {
            displayResults(testResult);
        }

        LOG_RESULT.info("End result: " + areAllTestResultsOk(testResultList));
    }

    private void displayResults(TestResult testResult) {
        LOG_RESULT.info(String.format("========================== Test: %s", testResult.getTestName()));

        for (CallResult result : testResult.getResults()) {
            LOG_RESULT.info(String.format("-------------- Call id: %s", result.getCall().getId()));
            LOG_RESULT.info(String.format("Url: %s", result.getCall().getUrl()));
            LOG_RESULT.info(String.format("Parameters: %s", result.getCall().getParameters()));
            LOG_RESULT.info(String.format("Result status: %s", result.getStatus()));
            try {
                LOG_RESULT.info(String.format("Response: %s", result.getContentExchange().getResponseContent()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            for (FieldResult fieldResult : result.getFieldResults()) {
                System.out.println(String.format("%10s | %50s | %50s | %50s", fieldResult.getStatus().name(), fieldResult.getExpectedValue(),
                        fieldResult.getReceivedValue(), fieldResult.getPath()));
            }
        }

        LOG_RESULT.info(String.format("========================== Result: %s", testResult.getTestStatus().name()));
        LOG_RESULT.info("==============================================================================");
    }
}
