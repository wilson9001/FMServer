package Handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import DataServices.Services;
import Requests.LoadRequest;
import Responses.LoadResponse;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Created by Alex on 10/28/2017.
 */

public class loadHandler implements HttpHandler
{
    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        System.out.println("Load Handler called");

        Gson gson = new Gson();

        if(!exchange.getRequestMethod().toLowerCase().equals(defaultHandler.requestTypePost))
        {
            LoadResponse loadResponse = new LoadResponse(defaultHandler.badRequestType);

            exchange.sendResponseHeaders(HTTP_BAD_REQUEST, 0);

            String errorJSON = gson.toJson(loadResponse);

            OutputStream os = exchange.getResponseBody();

            os.write(errorJSON.getBytes());

            os.close();

            return;
        }

        LoadRequest loadReq = gson.fromJson(new BufferedReader(new InputStreamReader(exchange.getRequestBody())), LoadRequest.class);

        LoadResponse loadRes  = new Services().processRequest(loadReq);

        String result = loadRes.getResult();

        int ServerResponseCode = HTTP_OK;

        if(result.equals(Services.internalSeverError))
        {
            ServerResponseCode = HTTP_INTERNAL_ERROR;
        }
        else if(result.equals(Services.badParameters))
        {
            ServerResponseCode = HTTP_BAD_REQUEST;
        }

        exchange.sendResponseHeaders(ServerResponseCode, 0);

        result = gson.toJson(loadRes);

        OutputStream os = exchange.getResponseBody();

        os.write(result.getBytes());

        os.close();
    }
}