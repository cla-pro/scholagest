package net.scholagest.tester;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import net.scholagest.tester.jaxb.TCall;

import org.mortbay.jetty.client.ContentExchange;
import org.mortbay.jetty.client.HttpClient;

public class CallHandler {
    private final ResponseAnalyzer responseAnalyzer;

    public CallHandler(ResponseAnalyzer responseAnalyzer) {
        this.responseAnalyzer = responseAnalyzer;
    }

    public void handleCallAndException(String baseUrl, TCall call) {
        try {
            ContentExchange contentExchange = handleCall(baseUrl, call);
            responseAnalyzer.analyzeContentExchange(call, contentExchange);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ContentExchange handleCall(String baseUrl, TCall call) throws Exception {
        HttpClient httpClient = new HttpClient();

        httpClient.start();

        try {
            ContentExchange contentExchange = new ContentExchange();

            httpClient.setConnectorType(HttpClient.CONNECTOR_SELECT_CHANNEL);

            String encoded = baseUrl + call.getUrl() + "?" + encodeParameters(call.getParameters());
            contentExchange.setURL(encoded);
            contentExchange.setMethod(call.getHttpMethod());

            httpClient.send(contentExchange);
            contentExchange.waitForDone();

            return contentExchange;
        } finally {
            httpClient.stop();
        }
    }

    private String encodeParameters(String parameters) throws UnsupportedEncodingException {
        String[] allParams = parameters.split("&");

        StringBuilder paramBuilder = new StringBuilder();
        boolean first = true;
        for (String param : allParams) {
            if (first) {
                first = false;
            } else {
                paramBuilder.append("&");
            }

            String[] keyValue = param.split("=");
            paramBuilder.append(keyValue[0]);
            paramBuilder.append("=");
            paramBuilder.append(URLEncoder.encode(keyValue[1], "UTF-8"));
        }

        return paramBuilder.toString();
    }
}
