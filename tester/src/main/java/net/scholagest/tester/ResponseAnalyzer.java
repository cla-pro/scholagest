package net.scholagest.tester;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import net.scholagest.tester.jaxb.TCall;
import net.scholagest.tester.jaxb.TResult;
import net.scholagest.tester.result.FieldResult;
import net.scholagest.tester.result.FieldResultStatus;
import net.scholagest.tester.result.TestResult;
import net.scholagest.tester.result.TestResultStatus;

import org.mortbay.jetty.HttpStatus;
import org.mortbay.jetty.client.ContentExchange;

import com.google.gson.Gson;
import com.google.gson.internal.StringMap;

public class ResponseAnalyzer {
    private List<TestResult> results;
    private final Placeholder placeholder;

    public ResponseAnalyzer(Placeholder placeholder) {
        this.placeholder = placeholder;
        this.results = new ArrayList<TestResult>();
    }

    public void analyzeContentExchange(TCall call, ContentExchange contentExchange) throws UnsupportedEncodingException {
        TestResult testResult = new TestResult();
        testResult.setCall(call);
        testResult.setContentExchange(contentExchange);

        if (isHtmlError(contentExchange)) {
            testResult.setStatus(TestResultStatus.HTML_ERROR);
        } else {
            List<FieldResult> fieldResultList = checkContent(call, contentExchange);
            testResult.setStatus(getTestResultStatus(fieldResultList));
            testResult.setFieldResults(fieldResultList);
        }

        results.add(testResult);
    }

    private TestResultStatus getTestResultStatus(List<FieldResult> fieldResultList) {
        for (FieldResult fieldResult : fieldResultList) {
            if (fieldResult.getStatus() != FieldResultStatus.OK) {
                return TestResultStatus.CONTENT_ERROR;
            }
        }
        return TestResultStatus.OK;
    }

    private boolean isHtmlError(ContentExchange contentExchange) {
        return contentExchange.getResponseStatus() != HttpStatus.ORDINAL_200_OK;
    }

    private List<FieldResult> checkContent(TCall call, ContentExchange contentExchange) throws UnsupportedEncodingException {
        String response = contentExchange.getResponseContent();
        List<FieldResult> fieldResultList = new ArrayList<>();

        for (TResult result : call.getExpectedResult().getResult()) {
            String path = placeholder.replacePlaceholdersInString(result.getPath());
            String resultValue = getValue(path, response);

            if (result.getValue() != null) {
                checkAndStoreResult(fieldResultList, result, resultValue);
            }

            if (result.getStoreIn() != null) {
                placeholder.storeValue(result.getStoreIn(), resultValue);
            }
        }

        return fieldResultList;
    }

    private void checkAndStoreResult(List<FieldResult> fieldResultList, TResult result, String resultValue) {
        FieldResult fieldResult = new FieldResult();
        fieldResult.setPath(result.getPath());
        fieldResult.setExpectedValue(result.getValue());
        fieldResult.setReceivedValue(resultValue);

        if (resultValue == null) {
            fieldResult.setStatus(FieldResultStatus.NO_RESULT);
        } else if (result.getValue().equals(resultValue)) {
            fieldResult.setStatus(FieldResultStatus.OK);
        } else {
            fieldResult.setStatus(FieldResultStatus.WRONG_RESULT);
        }

        fieldResultList.add(fieldResult);
    }

    @SuppressWarnings("unchecked")
    private String getValue(String path, String response) {
        String[] pathElements = path.split("/");

        StringMap<Object> converted = new Gson().fromJson(response, StringMap.class);
        return getValueRec(pathElements, 0, converted);
    }

    @SuppressWarnings("unchecked")
    private String getValueRec(String[] pathElements, int i, StringMap<Object> subJson) {
        Object fieldValue = subJson.get(pathElements[i]);

        if (pathElements.length - 1 == i || fieldValue == null) {
            if (fieldValue instanceof String) {
                return (String) fieldValue;
            } else {
                return null;
            }
        }

        if (fieldValue instanceof StringMap) {
            return getValueRec(pathElements, i + 1, (StringMap<Object>) fieldValue);
        }
        return null;
    }

    public void displayResults() {
        for (TestResult result : results) {
            System.out.println(String.format("============== Call id: %s", result.getCall().getId()));
            System.out.println(String.format("Url: %s", result.getCall().getUrl()));
            System.out.println(String.format("Parameters: %s", placeholder.replacePlaceholdersInString(result.getCall().getParameters())));
            System.out.println(String.format("Result status: %s", result.getStatus().name()));
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
    }
}
