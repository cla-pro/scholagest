package net.scholagest.tester.result;

import java.util.ArrayList;
import java.util.List;

import net.scholagest.tester.jaxb.TCall;

import org.mortbay.jetty.client.ContentExchange;

public class TestResult {
    private TCall call;
    private ContentExchange contentExchange;
    private TestResultStatus status;
    private List<FieldResult> fieldResults;

    public TestResult() {
        fieldResults = new ArrayList<>();
    }

    public TCall getCall() {
        return call;
    }

    public void setCall(TCall call) {
        this.call = call;
    }

    public ContentExchange getContentExchange() {
        return contentExchange;
    }

    public void setContentExchange(ContentExchange contentExchange) {
        this.contentExchange = contentExchange;
    }

    public TestResultStatus getStatus() {
        return status;
    }

    public void setStatus(TestResultStatus status) {
        this.status = status;
    }

    public List<FieldResult> getFieldResults() {
        return fieldResults;
    }

    public void setFieldResults(List<FieldResult> fieldResults) {
        this.fieldResults = fieldResults;
    }
}
