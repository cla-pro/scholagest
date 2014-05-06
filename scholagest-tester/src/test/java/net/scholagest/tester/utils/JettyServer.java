package net.scholagest.tester.utils;

import net.scholagest.app.rest.GuiceContext;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;

import com.google.inject.servlet.GuiceFilter;

/**
 * Handle the Jetty server for the test context.
 * 
 * @author CLA
 * @since 0.15.0
 */
public class JettyServer {
    static final int SERVER_PORT = 8080;

    private Server server;

    /**
     * Start the Jetty server
     * 
     * @throws Exception Jetty exception forwarded
     */
    public void start() throws Exception {
        server = new Server(SERVER_PORT);

        final ServletContextHandler handler = new ServletContextHandler(server, "/");
        handler.addEventListener(new GuiceContext("scholagest-test-pu"));
        handler.addFilter(new FilterHolder(GuiceFilter.class), "/*", null);
        handler.addServlet(DefaultServlet.class, "/");

        server.setHandler(handler);
        server.start();
    }

    /**
     * Stop the Jetty server
     * 
     * @throws Exception Jetty exception forwarded 
     */
    public void stop() throws Exception {
        server.stop();
    }
}
