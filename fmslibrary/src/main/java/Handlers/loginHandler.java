package Handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import DataServices.Services;
import Requests.LoginRequest;
import Responses.LoadResponse;
import Responses.LoginResponse;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Created by Alex on 10/28/2017.
 */

public class loginHandler implements HttpHandler
{
    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        System.out.println("Login Handler called");

        Gson gson = new Gson();

        if(!exchange.getRequestMethod().toLowerCase().equals(defaultHandler.requestTypePost))
        {
            LoginResponse loginResponse = new LoginResponse(defaultHandler.badRequestType);

            exchange.sendResponseHeaders(HTTP_BAD_REQUEST, 0);

            String errorJSON = gson.toJson(loginResponse);

            OutputStream os = exchange.getResponseBody();

            os.write(errorJSON.getBytes());

            os.close();

            return;
        }

        //LoginRequest loginReq = gson.fromJson(exchange.getRequestBody().toString(), LoginRequest.class);
        LoginRequest loginReq = gson.fromJson(new BufferedReader(new InputStreamReader(exchange.getRequestBody())), LoginRequest.class);

        LoginResponse loginResponse = new Services().processRequest(loginReq);

        if(loginResponse.getErrorMessage() != null)
        {
            String errorMessage = loginResponse.getErrorMessage();

            if(errorMessage.equals(Services.badParameters))
            {
                exchange.sendResponseHeaders(HTTP_BAD_REQUEST, 0);
            }

            else
            {
                exchange.sendResponseHeaders(HTTP_INTERNAL_ERROR, 0);
            }
        }
        else
        {
            exchange.sendResponseHeaders(HTTP_OK, 0);
        }

        OutputStream os = exchange.getResponseBody();

        String result = gson.toJson(loginResponse);

        os.write(result.getBytes());

        os.close();
    }
}
