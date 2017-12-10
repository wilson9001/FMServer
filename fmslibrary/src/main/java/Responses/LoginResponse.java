package Responses;

/**
 * Response object for login requests.
 * @author ajw9001
 */
public class LoginResponse
{
    /**
     * Overloaded constructor. Used when the response represents a success. The error flag will be set to <code>false</code>.
     * @param authToken
     * @param userName
     * @param personID
     */
    public LoginResponse(String authToken, String userName, String personID)
    {
        this.authToken = authToken;
        this.userName = userName;
        this.personID = personID;
        this.message = null;
    }

    /**
     * Overloaded constructor. Used when an error occurred. The error flag will be set to <code>true</code>.
     * @param errorMessage Error message.
     */
    public LoginResponse(String errorMessage)
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