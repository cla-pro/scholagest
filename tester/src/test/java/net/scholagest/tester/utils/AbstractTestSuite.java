package net.scholagest.tester.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpHeader;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public abstract class AbstractTestSuite {
    private static final String URL_QUERY_SEPARATOR = "?";
    private static final String URL_PARAMETER_SEPARATOR = "&";
    private static final String URL_KEY_VALUE_SEPARATOR = "=";

    private static final String SERVER_HOST = "http://localhost";

    private static H2Database h2Database;
    private static JettyServer jettyServer;

    private HttpClient httpClient;

    // Called before the implementations' @BeforeClass
    @BeforeClass
    public static void setUpClass() throws Exception {
        h2Database = new H2Database();
        h2Database.start();
        h2Database.initialize();

        jettyServer = new JettyServer();
        jettyServer.start();
    }

    // Called before the implementations' @Before
    @Before
    public void setUp() throws Exception {
        httpClient = new HttpClient();
        httpClient.start();
    }

    // Called after the implementations' @After
    @After
    public void tearDown() throws Exception {
        httpClient.stop();
    }

    // Called after the implementations' @AfterClass
    @AfterClass
    public static void tearDownClass() throws Exception {
        jettyServer.stop();
        h2Database.stop();
    }

    protected ContentResponse callGET(final String url, final List<UrlParameter> parameters) throws Exception {
        final String fullUrl = createUrlWithParameters(url, parameters);
        return httpClient.GET(fullUrl);
    }

    protected ContentResponse callPOST(final String url, final String content) throws Exception {
        final String fullUrl = createUrl(url);
        final Request postRequest = httpClient.POST(fullUrl).content(new StringContentProvider(content));
        postRequest.header(HttpHeader.AUTHORIZATION, "");
        return postRequest.send();
    }

    private String createUrl(final String url) throws UnsupportedEncodingException {
        return createUrlWithParameters(url, new ArrayList<UrlParameter>());
    }

    private String createUrlWithParameters(final String url, final List<UrlParameter> parameters) throws UnsupportedEncodingException {
        final String encoded = mergeParameters(parameters);

        final StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(SERVER_HOST);
        urlBuilder.append(":");
        urlBuilder.append(JettyServer.SERVER_PORT);
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
}
