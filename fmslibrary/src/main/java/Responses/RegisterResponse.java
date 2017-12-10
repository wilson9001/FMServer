package Responses;

import java.util.ArrayList;

/**
 * Response class for registration object.
 * @author ajw9001
 */
public class RegisterResponse
{
    /**
     * Constructor. Used when the registration was successful.
     * @param authToken
     * @param userName
     * @param personID
     */
    public RegisterResponse(String authToken, String userName, String personID)
    {
        this.authToken = authToken;
        this.userName = userName;
        this.personID = personID;
        this.message = null;
    }

    /**
     * Overloaded constructor. Used when there was an error.
     * @param errorMessage
     */
    public RegisterResponse(String errorMessage)
    {
        this.authToken = null;
        this.userName = null;
        this.personID = null;
        this.message = errorMessage;
    }

    private String authToken, userName, personID, message;

    public String getAuthToken()
    {
        return authToken;
    }

    public String getUserName()
    {
        return userName;
    }

    public String getPersonID()
    {
        return personID;
    }

    public String getErrorMessage()
    {
        return message;
    }
}
