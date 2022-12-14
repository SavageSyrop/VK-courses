package ru.vk.server;

import org.eclipse.jetty.server.*;

public class JettyServer {

    public static Server build() {
        HttpConfiguration configuration = new HttpConfiguration();
        Server server = new Server();
        HttpConnectionFactory connectionFactory = new HttpConnectionFactory(configuration);
        ServerConnector serverConnector = new ServerConnector(server, connectionFactory);
        serverConnector.setHost("localhost");
        serverConnector.setPort(3466);
        server.setConnectors(new Connector[]{serverConnector});
        return server;
    }
}
