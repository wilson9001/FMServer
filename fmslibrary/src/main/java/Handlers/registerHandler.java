package Handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import DataServices.Services;
import Requests.RegisterRequest;
import Requests.RegisterRequest1;
import Responses.RegisterResponse;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Created by Alex on 10/28/2017.
 */

public class registerHandler implements HttpHandler
{
    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        System.out.println("Register Handler called");

        Gson gson = new Gson();

        if(!exchange.getRequestMethod().toLowerCase().equals(defaultHandler.requestTypePost))
        {
            RegisterResponse registerResponse = new RegisterResponse(defaultHandler.badRequestType);

            exchange.sendResponseHeaders(HTTP_BAD_REQUEST, 0);

            String errorJSON = gson.toJson(registerResponse);

            System.out.println(errorJSON);

            OutputStream os = exchange.getResponseBody();

            os.write(errorJSON.getBytes());

            os.close();

            return;
        }

        System.out.println("\nRequest type is good\n");
        //TODO: add check to make sure future years aren't added into database.
//Need to change Gson parsers to match this setup. Also need to make sure that all Models and database columns are being assigned to each other properly.
//could be register request 1?
        RegisterRequest registerRequest = gson.fromJson(new BufferedReader(new InputStreamReader(exchange.getRequestBody())), RegisterRequest.class);

        System.out.println("Register request created.\nInformation is:");
        System.out.println(registerRequest.getUserName());
        System.out.println(registerRequest.getEmail());
        System.out.println(registerRequest.getFirstName());
        System.out.println(registerRequest.getLastName());
        System.out.println(registerRequest.getGender());
        System.out.println(registerRequest.getPassword());

        Services service = new Services();

        RegisterResponse registerResponse = service.processRequest(registerRequest);

        if(registerResponse.getErrorMessage() != null)
        {
            String error = registerResponse.getErrorMessage();

            if(error.equals(Services.internalSeverError))
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

        String responseJSON = gson.toJson(registerResponse);

        OutputStream os = exchange.getResponseBody();

        System.out.println(responseJSON);

        os.write(responseJSON.getBytes());

        os.close();
    }
}
