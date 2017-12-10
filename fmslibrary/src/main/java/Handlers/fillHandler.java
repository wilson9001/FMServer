package Handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import DataServices.Services;
import Requests.FillRequest;
import Responses.FillResponse;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Created by Alex on 10/28/2017.
 */

public class fillHandler implements HttpHandler
{
    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        System.out.println("Fill Handler called");

        Gson gson = new Gson();

        if(!exchange.getRequestMethod().toLowerCase().equals(defaultHandler.requestTypePost))
        {
            FillResponse fillResponse = new FillResponse(defaultHandler.badRequestType);

            exchange.sendResponseHeaders(HTTP_BAD_REQUEST, 0);

            String errorJSON = gson.toJson(fillResponse);

            OutputStream os = exchange.getResponseBody();

            os.write(errorJSON.getBytes());

            os.close();

            return;
        }

        URI uri = exchange.getRequestURI();

        String uriString = uri.toString();

        System.out.println(uriString);

        String[] uriParts = uriString.split("/");

        FillResponse fillResponse;

        FillRequest fillRequest;

        switch (uriParts.length)
        {
            case 3:
                fillRequest = new FillRequest(uriParts[2]);
                break;
            case 4:
                fillRequest = new FillRequest(uriParts[2], Integer.valueOf(uriParts[3]));
                break;
            default:
                fillResponse = new FillResponse(Services.badParameters);

                exchange.sendResponseHeaders(HTTP_BAD_REQUEST, 0);

                String errorJSON = gson.toJson(fillResponse);

                OutputStream os = exchange.getResponseBody();

                os.write(errorJSON.getBytes());

                os.close();

                return;
        }

        fillResponse = new Services().processRequest(fillRequest);

        String result = fillResponse.getResult();

        String resultJSON = gson.toJson(fillResponse);

        int statusCode = result.equals(Services.internalSeverError) ? HTTP_INTERNAL_ERROR : HTTP_OK;

        exchange.sendResponseHeaders(statusCode, 0);

        OutputStream os = exchange.getResponseBody();

        os.write(resultJSON.getBytes());

        os.close();
    }
}