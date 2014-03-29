package net.scholagest.tester;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.mortbay.io.Buffer;
import org.mortbay.io.ByteArrayBuffer;
import org.mortbay.jetty.client.ContentExchange;
import org.mortbay.jetty.client.HttpClient;

public abstract class AbstractTestSuite {
    private static final String URL_QUERY_SEPARATOR = "?";
    private static final String URL_PARAMETER_SEPARATOR = "&";
    private static final String URL_KEY_VALUE_SEPARATOR = "=";
    private static final int SERVER_PORT = 80;
    private static final String SERVER_HOST = "http://localhost";

    private HttpClient httpClient;

    // Called before the implementations' @BeforeClass
    @BeforeClass
    public void setUpClass() {
        // TODO Start the DB
        // TODO Apply the DB-Schema
        // TODO Start the server
    }

    // Called before the implementations' @Before
    @Before
    public void setUp() throws Exception {
        httpClient = new HttpClient();
        httpClient.start();
    }

    // Called before the implementations' @After
    @After
    public void tearDown() throws Exception {
        httpClient.stop();
    }

    // Called before the implementations' @AfterClass
    @AfterClass
    public void tearDownClass() {
        // TODO Stop the server
    }

    protected ContentExchange callGET(final String url, final List<UrlParameter> parameters) throws Exception {
        return callURL("GET", url, parameters, null);
    }

    protected ContentExchange callPOST(final String url, final String requestContent) throws Exception {
        return callURL("POST", url, new ArrayList<UrlParameter>(), requestContent);
    }

    protected ContentExchange callPUT(final String url, final List<UrlParameter> parameters, final String requestContent) throws Exception {
        return callURL("PUT", url, parameters, requestContent);
    }

    private ContentExchange callURL(final String httpMethod, final String url, final List<UrlParameter> parameters, final String requestContent)
            throws IOException, InterruptedException {
        final ContentExchange contentExchange = new ContentExchange();

        httpClient.setConnectorType(HttpClient.CONNECTOR_SELECT_CHANNEL);

        final String encoded = createUrl(url, parameters);
        contentExchange.setURL(encoded);
        contentExchange.setMethod(httpMethod);
        if (requestContent != null) {
            contentExchange.setRequestContent(convertToBuffer(requestContent));
        }

        httpClient.send(contentExchange);
        contentExchange.waitForDone();

        return contentExchange;
    }

    private String createUrl(final String url, final List<UrlParameter> parameters) throws UnsupportedEncodingException {
        final String encoded = mergeParameters(parameters);

        final StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(SERVER_HOST);
        urlBuilder.append(SERVER_PORT);
        urlBuilder.append(url);

        if (!encoded.isEmpty()) {
            urlBuilder.append(URL_QUERY_SEPARATOR);
            urlBuilder.append(encoded);
        }

        return urlBuilder.toString();
    }

    private String mergeParameters(final List<UrlParameter> parameters) throws UnsupportedEncodingException {
        final StringBuilder parametersBuilder = new StringBuilder();

        boolean first = true;
        for (final UrlParameter urlParameter : parameters) {
            if (first) {
                first = false;
            } else {
                parametersBuilder.append(URL_PARAMETER_SEPARATOR);
            }

            parametersBuilder.append(urlParameter.getKey());
            parametersBuilder.append(URL_KEY_VALUE_SEPARATOR);
            parametersBuilder.append(urlParameter.getEncodedValue());
        }

        return parametersBuilder.toString();
    }

    private Buffer convertToBuffer(final String requestContent) {
        return new ByteArrayBuffer(requestContent);
    }
}
