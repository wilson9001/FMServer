package Handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import DataServices.Services;
import Requests.PersonRequest;
import Responses.PersonResponse;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Created by Alex on 10/28/2017.
 */

public class personHandler implements HttpHandler
{
    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        System.out.println("Person Handler called");

        Gson gson = new Gson();

        if(!exchange.getRequestMethod().toLowerCase().equals(defaultHandler.requestTypeGet))
        {
            PersonResponse personRes = new PersonResponse(defaultHandler.badRequestType);

            exchange.sendResponseHeaders(HTTP_BAD_REQUEST, 0);

            String errorJSON = gson.toJson(personRes);

            OutputStream os = exchange.getResponseBody();

            os.write(errorJSON.getBytes());

            os.close();

            return;
        }

        Headers headers = exchange.getRequestHeaders();

        if(!headers.containsKey(defaultHandler.authTokenHeaderName))
        {
            PersonResponse personRes = new PersonResponse(Services.invalidAuthToken);

            exchange.sendResponseHeaders(HTTP_BAD_REQUEST, 0);

            String errorJSON = gson.toJson(personRes);

            OutputStream os = exchange.getResponseBody();

            os.write(errorJSON.getBytes());

            os.close();

            return;
        }

        String authTokenString = headers.getFirst(defaultHandler.authTokenHeaderName);

        URI uri = exchange.getRequestURI();

        String uriString = uri.toString();

        String[] uriParts = uriString.split("/");

        PersonResponse personResponse;

        PersonRequest personRequest;
        switch (uriParts.length)
        {
            case 2:
                personRequest = new PersonRequest(authTokenString, null);
                break;
            case 3:
                personRequest = new PersonRequest(authTokenString, uriParts[2]);
                break;
            default:
                personResponse = new PersonResponse(Services.badParameters);

                exchange.sendResponseHeaders(HTTP_BAD_REQUEST, 0);

                String errorJSON = gson.toJson(personResponse);

                OutputStream os = exchange.getResponseBody();

                os.write(errorJSON.getBytes());

                os.close();

                return;
        }

        personResponse = new Services().processRequest(personRequest);

        if(personResponse.getErrorMessage() != null)
        {
            String errorMsg = personResponse.getErrorMessage();

            if(errorMsg.equals(Services.internalSeverError))
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

        String responseJSON = gson.toJson(personResponse);

        OutputStream os = exchange.getResponseBody();

        os.write(responseJSON.getBytes());

        os.close();
    }
}
