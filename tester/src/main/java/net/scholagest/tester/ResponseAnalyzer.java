package net.scholagest.tester;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import net.scholagest.tester.jaxb.TCall;
import net.scholagest.tester.jaxb.TResult;
import net.scholagest.tester.result.CallResult;
import net.scholagest.tester.result.CallResultStatus;
import net.scholagest.tester.result.FieldResult;
import net.scholagest.tester.result.FieldResultStatus;
import net.scholagest.tester.result.TestResult;

import org.mortbay.jetty.HttpStatus;
import org.mortbay.jetty.client.ContentExchange;

import com.google.gson.Gson;
import com.google.gson.internal.StringMap;

public class ResponseAnalyzer {
    private TestResult testResult;
    private final Placeholder placeholder;

    public ResponseAnalyzer(Placeholder placeholder, String testName) {
        this.placeholder = placeholder;
        this.testResult = new TestResult(testName);
    }

    public void analyzeContentExchange(TCall call, ContentExchange contentExchange) throws UnsupportedEncodingException {
        CallResult callResult = new CallResult();
        callResult.setCall(call);
        callResult.setContentExchange(contentExchange);

        if (isHtmlError(contentExchange)) {
            callResult.setStatus(CallResultStatus.HTML_ERROR);
        } else {
            List<FieldResult> fieldResultList = checkContent(call, contentExchange);
            callResult.setStatus(getTestResultStatus(fieldResultList));
            callResult.setFieldResults(fieldResultList);
        }

        testResult.addCallResult(callResult);
    }

    private CallResultStatus getTestResultStatus(List<FieldResult> fieldResultList) {
        for (FieldResult fieldResult : fieldResultList) {
            if (fieldResult.getStatus() != FieldResultStatus.OK) {
                return CallResultStatus.CONTENT_ERROR;
            }
        }
        return CallResultStatus.OK;
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

        if (isResultExpectedButNotReceived(result, resultValue)) {
            fieldResult.setStatus(FieldResultStatus.NO_RESULT);
        } else if (isReceivedResultCorrect(result, resultValue)) {
            fieldResult.setStatus(FieldResultStatus.OK);
        } else {
            fieldResult.setStatus(FieldResultStatus.WRONG_RESULT);
        }

        fieldResultList.add(fieldResult);
    }

    private boolean isReceivedResultCorrect(TResult result, String resultValue) {
        if (result.getValue().equals("null")) {
            return resultValue == null;
        }

        return result.getValue().equals(resultValue);
    }

    private boolean isResultExpectedButNotReceived(TResult result, String resultValue) {
        return resultValue == null && !result.getValue().equals("null");
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

        if (fieldValue == null) {
            return null;
        } else if (pathElements.length - 1 == i) {
            if (fieldValue instanceof String) {
                return (String) fieldValue;
            } else {
                return fieldValue.toString();
            }
        }

        if (fieldValue instanceof StringMap) {
            return getValueRec(pathElements, i + 1, (StringMap<Object>) fieldValue);
        }
        return null;
    }

    public TestResult getTestResult() {
        return testResult;
    }
}
