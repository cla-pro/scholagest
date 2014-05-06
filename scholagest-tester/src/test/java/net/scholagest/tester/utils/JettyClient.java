package net.scholagest.tester.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpMethod;

/**
 * Utility class used to send requests to the in-memory server.
 * 
 * @author CLA
 * @since 0.16.0
 */
public class JettyClient {
    private static final String URL_QUERY_SEPARATOR = "?";
    private static final String URL_PARAMETER_SEPARATOR = "&";
    private static final String URL_KEY_VALUE_SEPARATOR = "=";

    private static final String SERVER_HOST = "http://localhost";

    private HttpClient httpClient;

    /**
     * Start the jetty client.
     * 
     * @throws Exception Exception forwarded
     */
    public void start() throws Exception {
        httpClient = new HttpClient();
        httpClient.start();
    }

    /**
     * Stop the jetty client. After calling this method, no more requests may be sent to the server.
     * 
     * @throws Exception Exception forwarded
     */
    public void stop() throws Exception {
        httpClient.stop();
    }

    public ContentResponse callGET(final String url, final List<UrlParameter> parameters, final String token) throws Exception {
        final String fullUrl = createUrlWithParameters(url, parameters);
        final Request request = httpClient.newRequest(fullUrl).method(HttpMethod.GET).header(HttpHeader.AUTHORIZATION, token);
        return request.send();
    }

    public ContentResponse callPOST(final String url, final String content, final String token) throws Exception {
        final String fullUrl = createUrl(url);
        final Request postRequest = httpClient.POST(fullUrl).content(new StringContentProvider(content));
        if (token != null) {
            postRequest.header(HttpHeader.AUTHORIZATION, token);
        }
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
