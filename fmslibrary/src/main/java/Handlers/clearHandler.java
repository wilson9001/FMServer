package Handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;

import DataServices.Services;
import Requests.ClearRequest;
import Responses.ClearResponse;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Created by Alex on 10/28/2017.
 */

public class clearHandler implements HttpHandler
{
    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        System.out.println("Clear Handler called");

        Gson gson = new Gson();

        if(!exchange.getRequestMethod().toLowerCase().equals(defaultHandler.requestTypePost))
        {
            System.out.println("Clear post type is incorrect");

            ClearResponse clearResponse = new ClearResponse(defaultHandler.badRequestType);

            exchange.sendResponseHeaders(HTTP_BAD_REQUEST, 0);

            String errorJSON = gson.toJson(clearResponse);

            OutputStream os = exchange.getResponseBody();

            os.write(errorJSON.getBytes());

            os.close();

            return;
        }

        ClearResponse clearResponse = new Services().processRequest(new ClearRequest());

        String responseJSON = new Gson().toJson(clearResponse);

        String response = clearResponse.getResponse();

        int statusCode = response.equals(Services.internalSeverError) ? HTTP_INTERNAL_ERROR : HTTP_OK;

        exchange.sendResponseHeaders(statusCode, 0);

        OutputStream os = exchange.getResponseBody();

        System.out.println(responseJSON);

        os.write(responseJSON.getBytes());

        os.close();
    }
}
