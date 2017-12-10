package Handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import DataServices.Services;
import Requests.EventRequest;
import Responses.EventResponse;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Created by Alex on 10/28/2017.
 */

public class eventHandler implements HttpHandler
{
    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        System.out.println("Event Handler called");

        Gson gson = new Gson();

        if(!exchange.getRequestMethod().toLowerCase().equals(defaultHandler.requestTypeGet))
        {
            EventResponse eventRes = new EventResponse(defaultHandler.badRequestType);

            exchange.sendResponseHeaders(HTTP_BAD_REQUEST, 0);

            String errorJSON = gson.toJson(eventRes);

            OutputStream os = exchange.getResponseBody();

            os.write(errorJSON.getBytes());

            os.close();

            return;
        }

        Headers headers = exchange.getRequestHeaders();

        if(!headers.containsKey(defaultHandler.authTokenHeaderName))
        {
            EventResponse eventResponse = new EventResponse(Services.invalidAuthToken);

            exchange.sendResponseHeaders(HTTP_BAD_REQUEST, 0);

            String errorJSON = gson.toJson(eventResponse);

            OutputStream os = exchange.getResponseBody();

            os.write(errorJSON.getBytes());

            os.close();

            return;
        }

        String authTokenString = headers.getFirst(defaultHandler.authTokenHeaderName);

        URI uri = exchange.getRequestURI();

        String uriString = uri.toString();

        String[] uriParts = uriString.split("/");

        EventResponse eventResponse;

        EventRequest eventRequest;

        switch (uriParts.length)
        {
            case 2:
                eventRequest = new EventRequest(null, authTokenString);
                break;
            case 3:
                eventRequest = new EventRequest(uriParts[2], authTokenString);
                break;
            default:
                eventResponse = new EventResponse(Services.badParameters);

                exchange.sendResponseHeaders(HTTP_BAD_REQUEST, 0);

                String errorJSON = gson.toJson(eventResponse);

                OutputStream os = exchange.getResponseBody();

                os.write(errorJSON.getBytes());

                os.close();

                return;
        }

        eventResponse = new Services().processRequest(eventRequest);

        String errorTest = eventResponse.getMessage();

        if(errorTest != null)
        {
            if(errorTest.equals(Services.internalSeverError))
            {
                exchange.sendResponseHeaders(HTTP_INTERNAL_ERROR, 0);
            }
            else
            {
                exchange.sendResponseHeaders(HTTP_BAD_REQUEST, 0);
            }
        }

        else
        {
            exchange.sendResponseHeaders(HTTP_OK, 0);
        }

        String responseJSON = gson.toJson(eventResponse);

        System.out.println(responseJSON);

        OutputStream os = exchange.getResponseBody();

        os.write(responseJSON.getBytes());

        os.close();
    }
}