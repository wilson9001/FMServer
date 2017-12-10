package DataServices;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

import Handlers.clearHandler;
import Handlers.defaultHandler;
import Handlers.eventHandler;
import Handlers.fillHandler;
import Handlers.loadHandler;
import Handlers.loginHandler;
import Handlers.personHandler;
import Handlers.registerHandler;

/**
 * Created by Alex on 11/1/2017.
 */

public class Server
{
    private static final int MAX_WAITING_CONNECTIONS = 12;

    private HttpServer server;

    private void run(String portNumber)
    {
        System.out.println("Initializing HTTP Server");

        try
        {
            server = HttpServer.create(
                    new InetSocketAddress(Integer.parseInt(portNumber)),
                    MAX_WAITING_CONNECTIONS);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }

        server.setExecutor(null);

        System.out.println("Creating contexts");

        server.createContext("/user/register", new registerHandler());
        server.createContext("/user/login", new loginHandler());
        server.createContext("/clear", new clearHandler());
        server.createContext("/fill/", new fillHandler());
        server.createContext("/load", new loadHandler());
        server.createContext("/person", new personHandler());
        server.createContext("/event", new eventHandler());
       // server.createContext("/css/main.css", new defaultHandler());
        server.createContext("/", new defaultHandler());

        //server.createContext("", new defaultHandler());

        System.out.println("Starting server");

        server.start();

        System.out.println("Server started");
    }

    // "args" should contain one command-line argument, which is the port number
    public static void main(String[] args) {
        String portNumber = args[0];
        new Server().run(portNumber);
    }
}
