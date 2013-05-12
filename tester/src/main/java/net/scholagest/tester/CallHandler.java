package net.scholagest.tester;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import net.scholagest.tester.jaxb.TCall;

import org.mortbay.io.Buffer;
import org.mortbay.io.ByteArrayBuffer;
import org.mortbay.jetty.client.ContentExchange;
import org.mortbay.jetty.client.HttpClient;

public class CallHandler {
    private final ResponseAnalyzer responseAnalyzer;
    private final Placeholder placeholder;

    public CallHandler(ResponseAnalyzer responseAnalyzer, Placeholder placeholder) {
        this.responseAnalyzer = responseAnalyzer;
        this.placeholder = placeholder;
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

            String httpMethod = call.getHttpMethod();
            String parameters = placeholder.replacePlaceholdersInString(call.getParameters());
            String encoded = createUrl(baseUrl, call, parameters, httpMethod);
            contentExchange.setURL(encoded);
            contentExchange.setMethod(httpMethod);
            if (httpMethod.equals("POST")) {
                contentExchange.setRequestContent(createContent(parameters));
            }

            httpClient.send(contentExchange);
            contentExchange.waitForDone();

            return contentExchange;
        } finally {
            httpClient.stop();
        }
    }

    private Buffer createContent(String parameters) throws UnsupportedEncodingException {
        return new ByteArrayBuffer(parameters);
    }

    private String createUrl(String baseUrl, TCall call, String parameters, String httpMethod) throws UnsupportedEncodingException {
        String url = baseUrl + call.getUrl();
        if (httpMethod.equals("GET")) {
            return url + "?" + encodeParameters(parameters);
        }
        return url;
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
            paramBuilder.append(encoreUrl(keyValue[1]));
        }

        return paramBuilder.toString();
    }

    private String encoreUrl(String toEncode) throws UnsupportedEncodingException {
        return URLEncoder.encode(toEncode, "UTF-8");
    }
}
