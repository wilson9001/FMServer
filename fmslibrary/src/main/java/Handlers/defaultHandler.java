package Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Created by Alex on 10/28/2017.
 */

public class defaultHandler implements HttpHandler
{

    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        System.out.println("Default Handler Called");

        URI uri = exchange.getRequestURI();

        System.out.print("Uri is: ");
        System.out.println(uri);

        String baseFilePath = "libs/web/";

        String filePathString = uri.toString();

       filePathString = filePathString.substring(1);

        if(filePathString.isEmpty())
        {
            filePathString = "index.html";
        }

        int HTTPResponseCode = HTTP_OK;

        filePathString = baseFilePath + filePathString;

        //System.out.println("File path string is:");
        //System.out.println(filePathString);

        File fileToFetch = new File(filePathString);

        if(!fileToFetch.exists())
        {
            filePathString = baseFilePath + "HTML/404.html";
            //filePathString = "C:/Users/Alex/AndroidStudioProjects/Application/fmslibrary/libs/web/HTML/404.html";
            System.out.println("Requested file not found");
            HTTPResponseCode = HTTP_NOT_FOUND;
        }

        Path filePath = FileSystems.getDefault().getPath(filePathString);//Paths.get(baseFilePath  + filePathString);

        exchange.sendResponseHeaders(HTTPResponseCode, 0);

        OutputStream os = exchange.getResponseBody();

        Files.copy(filePath,os);

        //PrintWriter printer = new PrintWriter(exchange.getResponseBody());

        //printer.write(new String(Files.readAllBytes(Paths.get(baseFilePath + filePathString)), StandardCharsets.UTF_8));
        //printer.close();

        os.close();
    }

    public static final String badRequestType = "Bad request type";
    public static final String requestTypeGet = "get";
    public static final String requestTypePost = "post";
    public static final String authTokenHeaderName = "Authorization";
}
