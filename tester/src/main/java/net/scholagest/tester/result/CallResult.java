package net.scholagest.tester.result;

import java.util.ArrayList;
import java.util.List;

import net.scholagest.tester.jaxb.TCall;

import org.mortbay.jetty.client.ContentExchange;

public class CallResult {
    private TCall call;
    private ContentExchange contentExchange;
    private CallResultStatus status;
    private List<FieldResult> fieldResults;

    public CallResult() {
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

    public CallResultStatus getStatus() {
        return status;
    }

    public void setStatus(CallResultStatus status) {
        this.status = status;
    }

    public List<FieldResult> getFieldResults() {
        return fieldResults;
    }

    public void setFieldResults(List<FieldResult> fieldResults) {
        this.fieldResults = fieldResults;
    }
}
